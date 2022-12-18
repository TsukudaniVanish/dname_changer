package domain

enum Command {
    case ListSegments(pageMax: Option[String], pageNum: Option[String], isFileOnly: Boolean, isDirOnly: Boolean)
    case Quit()
}