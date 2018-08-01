#!/usr/bin/python
#-*- coding: UTF-8 -*-
#
# @file: logger.py
#   python2.7.x logger function
#
# @create: 2017-12-08
#
# @update: 2018-07-23 18:44:39
#
################################################################################

def set_logger(logger, log_path, log_level):
    import os, sys
    import utility as util
    import evntlog as elog

    # set logger:
    logpath = util.source_abspath(log_path)
    if not os.path.isdir(logpath):
        util.error("log path not found: %r" % logpath)
        sys.exit(-1)

    logging_config = logger['logging_config']
    if not util.file_exists(logging_config):
        util.error("logging.config file not found: %s" % logging_config)
        sys.exit(-1)

    # init logger: main
    try:
        logger_file = os.path.join(logpath, logger['file'])

        loggingConfigDict = elog.init_logger(
            logger_name = logger['name'],
            logging_config = logging_config,
            logger_file = logger_file,
            logger_level = log_level)

        elog.force("logging config : %s", logging_config)
        elog.force("logger file    : %s", logger_file)
        elog.force("logger level   : %s", log_level)
        elog.force("logger name    : %s", logger['name'])

        if not loggingConfigDict:
            util.error("logging config file error: %s" % logging_config)
            sys.exit(-1)

        return loggingConfigDict
    except Exception as ex:
        util.error("error init logger: %r" % ex)
        sys.exit(-1)
