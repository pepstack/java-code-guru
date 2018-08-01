#-*- coding: UTF-8 -*-
# @file: graceful_exit_event.py
#
#   此文件必须仅仅在主 app 中被引入一次
#
# @refer:
#   http://stackoverflow.com/questions/26414704/how-does-a-python-process-exit-gracefully-after-receiving-sigterm-while-waiting?rq=1
#   http://www.cnblogs.com/kaituorensheng/p/4445418.html
#
# @author: master@pepstack
#
# @create: 2016-07-13
#
# @update: 2018-07-24 15:02:35
#
# @version: 2018-07-24 15:02:35
#
#######################################################################
import os, signal, time, multiprocessing

#######################################################################
# 全局变量
global_exit_event = multiprocessing.Event()


class GracefulExitEvent(object):
    @staticmethod
    def onSigTerm(signum, frame):
        global_exit_event.set()

    @staticmethod
    def onSigChld(signo, frame):
        pid, status = os.waitpid(-1, os.WNOHANG)
        global_exit_event.set()

    @staticmethod
    def onSigInt(signo, frame):
        global_exit_event.set()


    def __init__(self):
        self.workers = []

        self.exit_event = global_exit_event

        # Use signal handler to throw exception which can be caught
        # by worker process to allow graceful exit.
        signal.signal(signal.SIGTERM, GracefulExitEvent.onSigTerm)
        signal.signal(signal.SIGCHLD, GracefulExitEvent.onSigChld)
        signal.signal(signal.SIGINT, GracefulExitEvent.onSigInt)
        pass


    def register_worker(self, wp):
        self.workers.append(wp)
        pass


    def start_workers(self):
        for wp in self.workers:
            # According to multiprocess daemon documentation by setting daemon=True
            #  when your script ends its job will try to kill all subprocess.
            # That occurs before they can start to write so no output will be produced.
            wp.daemon = False

            wp.start()
        pass


    def is_stop(self):
        return self.exit_event.is_set()


    def notify_stop(self):
        self.exit_event.set()


    def loop_forever(self, interval_seconds = 1, cb_interval_func = None, func_data = None):
        counter = 0

        interval = interval_seconds * 10

        while not self.is_stop():
            time.sleep(0.1)

            counter += 1

            if counter == interval:
                counter = 0

                if cb_interval_func:
                    cb_interval_func(func_data)

                pass
        pass
