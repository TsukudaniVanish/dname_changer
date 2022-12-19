package domain

enum Command {
    case ListSegments(pageMax: Int, pageNum: Int, isFileOnly: Boolean, isDirOnly: Boolean)
    case Quit()
    case Error()
}