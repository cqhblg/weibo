package com.neo.sk.todos2018.shared.ptcl

/**
  * User: sky
  * Date: 2018/6/1
  * Time: 15:45
  *
  * update by zhangtao: 2019-3-23
  *
  */
object ToDoListProtocol {



  //添加记录
  case class AddRecordReq(content: String)
  case class AddBlogCommentReq(content:String)

  case class DelRecordReq(id: Int,username:String)
  case class LikesRecordReq(id:Int,username:String)

  case class DelBlogComment(id:Int,author:String)

  case class GetOtherBlog(name:String)
  case class GetSelfBlog(name:String)

  case class BlogComment(
    comment_id:Int,
    author:String,
    content:String,
    time:Long
  )

  case class GiveAttentionRX(
    author:String
  )

  case class AttentionUser(
    username:String
                          )
  case class BlogRecord(
    id:Int,
    username:String,
    content:String,
    time:Long
  )
  case class BlogIdReq(
   id:Int
   )

  //获得列表
  case class TaskRecord(
    id: Int,
    content: String,
    time: Long
  )
  case class GetListRsp(
    list: Option[List[BlogRecord]],
    errCode: Int = 0,
    msg: String = "Ok"
  ) extends CommonRsp
  case class GetAttentionUserRsp(
   list: Option[List[AttentionUser]],
  errCode: Int = 0,
  msg: String = "Ok"
  ) extends CommonRsp
  case class GetCommentListRsp(
    list: Option[List[BlogComment]],
    errCode: Int = 0,
    msg: String = "Ok"
  )extends CommonRsp
  case class UserItem(
    name:String,
    password:String
  )
}
