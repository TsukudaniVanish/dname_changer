package domain

enum Command {
    case ListSegments(pageMax: Int, pageNum: Int, isFileOnly: Boolean, isDirOnly: Boolean, name: String = "")
    case Quit()
    case Error()
}