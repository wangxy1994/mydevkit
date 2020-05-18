package com.wangxy.exoskeleton.controller;

public class ReplaceTest {
    public static void main(String[] args){
        System.out.println("hello");
        String line = "asynExecute('bfm.basedata.virtualcombi.VirtualCombiUpdate.service?resCode=virtualCombi&opCode=delService',res,succ, null,'正在删除+++') ;";
        System.out.println(line.replaceAll(",'(.*)\\+{3,}'",",processing"));

    }
}
