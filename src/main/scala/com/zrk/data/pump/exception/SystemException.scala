package com.zrk.data.pump.exception

import com.zrk.data.pump.result.CodeMsg

class SystemException extends RuntimeException{
  
  private final val serialVersionUID: Long = 1L

   private var cm: CodeMsg = _

    def this(cm: CodeMsg) {
        this()
        this.cm = cm
    }

    def getCm() = cm

    /** 这里直接打印出传入的异常信息.*/
    override def toString() = cm.toString()
}