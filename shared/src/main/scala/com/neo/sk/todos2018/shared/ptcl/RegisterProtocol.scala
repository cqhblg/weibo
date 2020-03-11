package com.neo.sk.todos2018.shared.ptcl

object RegisterProtocol {
  case class UserRegisterReq(
     userName: String,
     password: String
   )

  case class UserRegisterRsp(
     errCode: Int,
     msg: String
   ) extends CommonRsp
}
