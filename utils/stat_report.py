#!/usr/bin/python
#-*- coding: UTF-8 -*-
#
# @file: stat_report.py
#    statistics and report in main app with child processes
#
# @author: ZhangLiang, 350137278@qq.com
#
#
# @create: 2018-06-25
#
# @update: 2018-07-23 18:44:39
#
########################################################################
import os, time, inspect

import multiprocessing
from multiprocessing import Manager

########################################################################
import utility as util
import evntlog as elog

# ! DO NOT CHANGE BELOW !
logger_module_name, _ = os.path.splitext(os.path.basename(inspect.getfile(inspect.currentframe())))

########################################################################

class StatReport(object):

    def __init__(self, report_title, interval_seconds):
        self.lock = Manager().Lock()

        self.rows_dict = Manager().dict()

        self.keys_dict = Manager().dict()

        self.start_time = time.time()
        self.curr_time = self.start_time

        self._loop_counter = 0

        self.report_title = report_title
        self.interval_seconds = interval_seconds
        pass


    def enter_lock(self):
        self.lock.acquire()
        pass


    def leave_lock(self):
        self.lock.release()
        pass


    def set_stat_row(self, rowkey, rowdata = None):
        self.rows_dict[rowkey] = rowdata
        pass


    def get_stat_row(self, rowkey):
        return self.rows_dict.get(rowkey)


    def save_key_value(self, key, newval, defval = 0):
        oldval = self.keys_dict.get(key, defval)
        self.keys_dict[key] = newval

        if newval > oldval:
            arrow = "↑"
        elif newval < oldval:
            arrow = "↓"
        else:
            arrow = " "
        return (arrow, oldval)


    def update_time(self):
        self.curr_time = time.time()
        return self.curr_time


    def elapsed_seconds(self):
        d = self.curr_time - self.start_time;
        if d <= 0.0001:
            d = 0.001
        return d


    def interval_report(self, callback_report):
        self._loop_counter += 1

        if self._loop_counter == self.interval_seconds:
            self._loop_counter = 0

            self.update_time()

            callback_report(self)
        pass
