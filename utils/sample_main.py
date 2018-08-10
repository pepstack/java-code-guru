#!/usr/bin/python
#-*- coding: UTF-8 -*-
#
# @file: sample_main.py
#
#   sample for main
#
# @author: master@pepstack
#
# @create: 2018-07-24 14:59:45
#
# @update: 2018-08-10 09:49:03
#
# @version: 2018-08-10 09:49:03
#
########################################################################
import os, sys, stat, signal, shutil, inspect, commands, time, datetime

import yaml, jinja2

import multiprocessing
from multiprocessing import Process, Queue, Manager
from Queue import Empty, Full

import optparse, ConfigParser

########################################################################
# application specific
APPFILE = os.path.realpath(sys.argv[0])
APPHOME = os.path.dirname(APPFILE)
APPNAME,_ = os.path.splitext(os.path.basename(APPFILE))
APPVER = "0.0.1"
APPHELP = "sample for main."

########################################################################
# import your local modules
import utils.utility as util
import utils.evntlog as elog

from utils.graceful_exit_event import GracefulExitEvent
from utils.process_stat import ProcessStat

########################################################################
def produce_worker(gee, plugins_dict):
    try:
        while not gee.is_stop():
            elog.debug("produce worker pid(%d) running ...", os.getpid())
            time.sleep(1)
    except:
        pass
    finally:
        elog.warn("pid(%d) exit", os.getpid())


########################################################################
def sweep_worker(gee, plugins_dict):
    while not gee.is_stop():
        elog.debug("child pid(%d) running ...", os.getpid())
        time.sleep(1)

    elog.warn("pid(%d) exit", os.getpid())


########################################################################
def main_proc(gee):
    elog.info("main pid(%d) running ...", os.getpid())


########################################################################
# main entry function
#
def main(parser, appConfig, loggerConfig):
    import utils.logger

    (options, args) = parser.parse_args(args=None, values=None)

    loggerDictConfig = utils.logger.set_logger(loggerConfig, options.log_path, options.log_level)

    stopfile = appConfig['stopfile']
    if options.forcestop:
        elog.warn("create stop file: %s", stopfile)
        os.mknod(stopfile)
        sys.exit(0)

    # 监控文件路径
    watch_groups = util.parse_path_groups(options.watch_paths)
    if len(watch_groups) == 0:
        elog.error("watch paths not found. using: --watch-paths=PATHS")
        sys.exit(-1)

    # 插件配置
    plugins_dict = {}
    for plugin in appConfig['plugins_enabled']:
        plugin_yaml = os.path.join(APPHOME, 'conf' , 'plugins', plugin + ".yaml")
        elog.info("load plugin: %s", plugin_yaml)
        plugins_dict[plugin] = {}
        try:
            fd = open(plugin_yaml)
            with fd:
                plugin_dict = yaml.load(fd)

                table_schema = plugin_dict['table_schema']
                kafka_topic = plugin_dict['kafka_topic']

                plugins_dict[plugin] = {
                    'table_schema': table_schema,
                    'kafka_topic': kafka_topic,
                }
        except:
            elog.error("failed to load: %s", plugin_yaml)
            sys.exit(-1)

    elog.force("%s-%s starting", APPNAME, APPVER)

    # 打印options配置文件的值
    util.print_options_attrs(options, ['watch_paths', 'num_workers'])

    mgr = Manager()

    # 全局共享字典, 防止文件重复处理
    # https://stackoverflow.com/questions/6832554/python-multiprocessing-how-do-i-share-a-dict-among-multiple-processes
    dictLogfile = mgr.dict()

    lock = mgr.Lock()

    gee = GracefulExitEvent(lock, os.path.join(APPHOME, APPNAME))

    # 创建任务队列
    elog.info("create sweep queue")
    sweep_queue = Queue(100)

    # 创建 producers
    for i in xrange(options.num_workers):
        wp = multiprocessing.Process(name="", target=produce_worker, args=(gee, stopfile, plugins_dict))
        gee.register_worker(wp)

    # 创建 sweeps
    for i in xrange(len(watch_groups)):
        wp = multiprocessing.Process(name="", target=sweep_worker, args=(gee, stopfile, plugins_dict))
        gee.register_worker(wp)

    # start all child procs
    gee.start_workers()

    # main process wait forever
    gee.forever(3, main_proc, gee)

    elog.force("%s-%s exit.", APPNAME, APPVER)
    pass


########################################################################
# Usage:
#
#
if __name__ == "__main__":
    parser, group, optparse = util.init_parser_group(
        appname = APPNAME,
        appver = APPVER,
        apphelp = APPHELP,
        usage = "%prog [Options]",
        group_options = os.path.join(APPHOME, "options/group_options.yaml"))

    # 如果 STOP 文件存在, 则终止服务. 程序启动时自动删除这个文件
    stopfile = os.path.join(APPHOME, APPNAME + ".FORCESTOP")
    util.remove_file_nothrow(stopfile)

    # 应用程序的缺省配置
    appConfig = {
        'stopfile' : stopfile,
        'plugins_enabled' : ['passport_login']
    }

    # 应用程序的日志配置
    logConfig = {
        'logging_config': os.path.join(APPHOME, 'conf/logger.config'),
        'file': APPNAME + '.log',
        'name': 'main'
    }

    # 主函数
    main(parser, appConfig, logConfig)

    # 程序正常退出
    sys.exit(0)
