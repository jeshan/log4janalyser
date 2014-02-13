/*
 * The Simplified BSD Licence follows:
 * Copyright (c) 2014, Jeshan G. BABOOA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 */

package co.jeshan.code.log4janalyser.utils.extended

import org.apache.log4j.spi.ThrowableInformation

/**
 * User: jeshan
 * Date: 27/01/14
 * Time: 19:47
 */
class ThrowableInformationExtended(throwableInfo: ThrowableInformation) extends ThrowableInformation(throwableInfo.getThrowableStrRep) {
    val throwableSrStep = throwableInfo.getThrowableStrRep

    def canEqual(other: Any): Boolean = other.isInstanceOf[ThrowableInformationExtended]

    override def equals(other: Any): Boolean = other match {
        case that: ThrowableInformationExtended =>
            (that canEqual this) &&
                throwableSrStep == that.throwableSrStep
        case _ => false
    }

    override def hashCode(): Int = {
        if (throwableSrStep == null) {
            return 1
        }
        val state = Seq(throwableSrStep)
        state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
    }

    override def toString = if (throwableSrStep == null) "" else super.toString
}
