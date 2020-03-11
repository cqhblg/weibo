package com.neo.sk.todos2018.models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object SlickTables extends {
  val profile = slick.jdbc.H2Profile
} with SlickTables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait SlickTables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(tBlogAttention.schema, tBlogComment.schema, tBlogLike.schema, tRecordInfo.schema, tUserBlog.schema, tUserInfo.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tBlogAttention
   *  @param giveUser Database column GIVE_USER SqlType(VARCHAR), Length(20,true)
   *  @param getUser Database column GET_USER SqlType(VARCHAR), Length(20,true) */
  case class rBlogAttention(giveUser: String, getUser: String)
  /** GetResult implicit for fetching rBlogAttention objects using plain SQL queries */
  implicit def GetResultrBlogAttention(implicit e0: GR[String]): GR[rBlogAttention] = GR{
    prs => import prs._
    rBlogAttention.tupled((<<[String], <<[String]))
  }
  /** Table description of table BLOG_ATTENTION. Objects of this class serve as prototypes for rows in queries. */
  class tBlogAttention(_tableTag: Tag) extends profile.api.Table[rBlogAttention](_tableTag, "BLOG_ATTENTION") {
    def * = (giveUser, getUser) <> (rBlogAttention.tupled, rBlogAttention.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(giveUser), Rep.Some(getUser))).shaped.<>({r=>import r._; _1.map(_=> rBlogAttention.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column GIVE_USER SqlType(VARCHAR), Length(20,true) */
    val giveUser: Rep[String] = column[String]("GIVE_USER", O.Length(20,varying=true))
    /** Database column GET_USER SqlType(VARCHAR), Length(20,true) */
    val getUser: Rep[String] = column[String]("GET_USER", O.Length(20,varying=true))

    /** Primary key of tBlogAttention (database name ATTENTION_KEY) */
    val pk = primaryKey("ATTENTION_KEY", (giveUser, getUser))

    /** Foreign key referencing tUserInfo (database name GET_ATTENTION_FK) */
    lazy val tUserInfoFk1 = foreignKey("GET_ATTENTION_FK", getUser, tUserInfo)(r => r.userName, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing tUserInfo (database name GIVE_ATTENTION_FK) */
    lazy val tUserInfoFk2 = foreignKey("GIVE_ATTENTION_FK", giveUser, tUserInfo)(r => r.userName, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table tBlogAttention */
  lazy val tBlogAttention = new TableQuery(tag => new tBlogAttention(tag))

  /** Entity class storing rows of table tBlogComment
   *  @param commentId Database column COMMENT_ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param blogId Database column BLOG_ID SqlType(INTEGER)
   *  @param commentUser Database column COMMENT_USER SqlType(VARCHAR), Length(20,true)
   *  @param commentContent Database column COMMENT_CONTENT SqlType(VARCHAR), Length(500,true)
   *  @param time Database column TIME SqlType(BIGINT) */
  case class rBlogComment(commentId: Int, blogId: Int, commentUser: String, commentContent: String, time: Long)
  /** GetResult implicit for fetching rBlogComment objects using plain SQL queries */
  implicit def GetResultrBlogComment(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[rBlogComment] = GR{
    prs => import prs._
    rBlogComment.tupled((<<[Int], <<[Int], <<[String], <<[String], <<[Long]))
  }
  /** Table description of table BLOG_COMMENT. Objects of this class serve as prototypes for rows in queries. */
  class tBlogComment(_tableTag: Tag) extends profile.api.Table[rBlogComment](_tableTag, "BLOG_COMMENT") {
    def * = (commentId, blogId, commentUser, commentContent, time) <> (rBlogComment.tupled, rBlogComment.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(commentId), Rep.Some(blogId), Rep.Some(commentUser), Rep.Some(commentContent), Rep.Some(time))).shaped.<>({r=>import r._; _1.map(_=> rBlogComment.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column COMMENT_ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val commentId: Rep[Int] = column[Int]("COMMENT_ID", O.AutoInc, O.PrimaryKey)
    /** Database column BLOG_ID SqlType(INTEGER) */
    val blogId: Rep[Int] = column[Int]("BLOG_ID")
    /** Database column COMMENT_USER SqlType(VARCHAR), Length(20,true) */
    val commentUser: Rep[String] = column[String]("COMMENT_USER", O.Length(20,varying=true))
    /** Database column COMMENT_CONTENT SqlType(VARCHAR), Length(500,true) */
    val commentContent: Rep[String] = column[String]("COMMENT_CONTENT", O.Length(500,varying=true))
    /** Database column TIME SqlType(BIGINT) */
    val time: Rep[Long] = column[Long]("TIME")

    /** Foreign key referencing tUserBlog (database name ID_COMMENT_FK) */
    lazy val tUserBlogFk = foreignKey("ID_COMMENT_FK", blogId, tUserBlog)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing tUserInfo (database name NAME_COMMENT_FK) */
    lazy val tUserInfoFk = foreignKey("NAME_COMMENT_FK", commentUser, tUserInfo)(r => r.userName, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table tBlogComment */
  lazy val tBlogComment = new TableQuery(tag => new tBlogComment(tag))

  /** Entity class storing rows of table tBlogLike
   *  @param id Database column ID SqlType(INTEGER)
   *  @param likeUser Database column LIKE_USER SqlType(VARCHAR), Length(20,true) */
  case class rBlogLike(id: Int, likeUser: String)
  /** GetResult implicit for fetching rBlogLike objects using plain SQL queries */
  implicit def GetResultrBlogLike(implicit e0: GR[Int], e1: GR[String]): GR[rBlogLike] = GR{
    prs => import prs._
    rBlogLike.tupled((<<[Int], <<[String]))
  }
  /** Table description of table BLOG_LIKE. Objects of this class serve as prototypes for rows in queries. */
  class tBlogLike(_tableTag: Tag) extends profile.api.Table[rBlogLike](_tableTag, "BLOG_LIKE") {
    def * = (id, likeUser) <> (rBlogLike.tupled, rBlogLike.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(likeUser))).shaped.<>({r=>import r._; _1.map(_=> rBlogLike.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER) */
    val id: Rep[Int] = column[Int]("ID")
    /** Database column LIKE_USER SqlType(VARCHAR), Length(20,true) */
    val likeUser: Rep[String] = column[String]("LIKE_USER", O.Length(20,varying=true))

    /** Primary key of tBlogLike (database name LIKE_KEY) */
    val pk = primaryKey("LIKE_KEY", (id, likeUser))

    /** Foreign key referencing tUserBlog (database name ID_LIKES_FK) */
    lazy val tUserBlogFk = foreignKey("ID_LIKES_FK", id, tUserBlog)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing tUserInfo (database name NAME_LIKES_FK) */
    lazy val tUserInfoFk = foreignKey("NAME_LIKES_FK", likeUser, tUserInfo)(r => r.userName, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table tBlogLike */
  lazy val tBlogLike = new TableQuery(tag => new tBlogLike(tag))

  /** Entity class storing rows of table tRecordInfo
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param author Database column AUTHOR SqlType(VARCHAR), Length(63,true)
   *  @param content Database column CONTENT SqlType(VARCHAR), Length(1023,true)
   *  @param time Database column TIME SqlType(BIGINT) */
  case class rRecordInfo(id: Int, author: String, content: String, time: Long)
  /** GetResult implicit for fetching rRecordInfo objects using plain SQL queries */
  implicit def GetResultrRecordInfo(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[rRecordInfo] = GR{
    prs => import prs._
    rRecordInfo.tupled((<<[Int], <<[String], <<[String], <<[Long]))
  }
  /** Table description of table RECORD_INFO. Objects of this class serve as prototypes for rows in queries. */
  class tRecordInfo(_tableTag: Tag) extends profile.api.Table[rRecordInfo](_tableTag, "RECORD_INFO") {
    def * = (id, author, content, time) <> (rRecordInfo.tupled, rRecordInfo.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(author), Rep.Some(content), Rep.Some(time))).shaped.<>({r=>import r._; _1.map(_=> rRecordInfo.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column AUTHOR SqlType(VARCHAR), Length(63,true) */
    val author: Rep[String] = column[String]("AUTHOR", O.Length(63,varying=true))
    /** Database column CONTENT SqlType(VARCHAR), Length(1023,true) */
    val content: Rep[String] = column[String]("CONTENT", O.Length(1023,varying=true))
    /** Database column TIME SqlType(BIGINT) */
    val time: Rep[Long] = column[Long]("TIME")
  }
  /** Collection-like TableQuery object for table tRecordInfo */
  lazy val tRecordInfo = new TableQuery(tag => new tRecordInfo(tag))

  /** Entity class storing rows of table tUserBlog
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param userName Database column USER_NAME SqlType(VARCHAR), Length(20,true)
   *  @param blogContent Database column BLOG_CONTENT SqlType(VARCHAR), Length(500,true)
   *  @param time Database column TIME SqlType(BIGINT) */
  case class rUserBlog(id: Int, userName: String, blogContent: String, time: Long)
  /** GetResult implicit for fetching rUserBlog objects using plain SQL queries */
  implicit def GetResultrUserBlog(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[rUserBlog] = GR{
    prs => import prs._
    rUserBlog.tupled((<<[Int], <<[String], <<[String], <<[Long]))
  }
  /** Table description of table USER_BLOG. Objects of this class serve as prototypes for rows in queries. */
  class tUserBlog(_tableTag: Tag) extends profile.api.Table[rUserBlog](_tableTag, "USER_BLOG") {
    def * = (id, userName, blogContent, time) <> (rUserBlog.tupled, rUserBlog.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(userName), Rep.Some(blogContent), Rep.Some(time))).shaped.<>({r=>import r._; _1.map(_=> rUserBlog.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column USER_NAME SqlType(VARCHAR), Length(20,true) */
    val userName: Rep[String] = column[String]("USER_NAME", O.Length(20,varying=true))
    /** Database column BLOG_CONTENT SqlType(VARCHAR), Length(500,true) */
    val blogContent: Rep[String] = column[String]("BLOG_CONTENT", O.Length(500,varying=true))
    /** Database column TIME SqlType(BIGINT) */
    val time: Rep[Long] = column[Long]("TIME")

    /** Foreign key referencing tUserInfo (database name NAME_FK) */
    lazy val tUserInfoFk = foreignKey("NAME_FK", userName, tUserInfo)(r => r.userName, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table tUserBlog */
  lazy val tUserBlog = new TableQuery(tag => new tUserBlog(tag))

  /** Entity class storing rows of table tUserInfo
   *  @param userName Database column USER_NAME SqlType(VARCHAR), PrimaryKey, Length(20,true)
   *  @param userPas Database column USER_PAS SqlType(VARCHAR), Length(20,true) */
  case class rUserInfo(userName: String, userPas: String)
  /** GetResult implicit for fetching rUserInfo objects using plain SQL queries */
  implicit def GetResultrUserInfo(implicit e0: GR[String]): GR[rUserInfo] = GR{
    prs => import prs._
    rUserInfo.tupled((<<[String], <<[String]))
  }
  /** Table description of table USER_INFO. Objects of this class serve as prototypes for rows in queries. */
  class tUserInfo(_tableTag: Tag) extends profile.api.Table[rUserInfo](_tableTag, "USER_INFO") {
    def * = (userName, userPas) <> (rUserInfo.tupled, rUserInfo.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(userName), Rep.Some(userPas))).shaped.<>({r=>import r._; _1.map(_=> rUserInfo.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column USER_NAME SqlType(VARCHAR), PrimaryKey, Length(20,true) */
    val userName: Rep[String] = column[String]("USER_NAME", O.PrimaryKey, O.Length(20,varying=true))
    /** Database column USER_PAS SqlType(VARCHAR), Length(20,true) */
    val userPas: Rep[String] = column[String]("USER_PAS", O.Length(20,varying=true))
  }
  /** Collection-like TableQuery object for table tUserInfo */
  lazy val tUserInfo = new TableQuery(tag => new tUserInfo(tag))
}
