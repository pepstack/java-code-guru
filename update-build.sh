#!/bin/bash
# @file: update-src.sh
#
#   update all source file.
#
# @author: master@pepstack.com
#
# @create: 2018-05-18
# @update: 2018-05-28
#
#######################################################################
# will cause error on macosx
_file=$(readlink -f $0)

_cdir=$(dirname $_file)
_name=$(basename $_file)

#######################################################################

$_cdir/pysrc/updt.py \
    --path=$_cdir/src \
    --filter="java" \
    --author="master@pepstack.com" \
    --recursive

mvn clean compile
