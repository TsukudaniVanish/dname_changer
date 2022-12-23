package domain

import scala.compiletime.ops.boolean
class LSInput(
    pageMax: Int= 10, 
    pageSize: Int = 20, 
    isFileOnly: Boolean = false, 
    isDirOnly: Boolean = false, 
    name: String = "",
    parentId: Option[String] = None
) {
    val PageNum: Int = pageMax
    val PageSize: Int = pageSize
    val IsFileOnly: Boolean = isFileOnly
    val IsDirOnly: Boolean = isDirOnly
    val Name: String = name 
    val OptParentId: Option[String] = parentId
}

enum Command {
    case ListSegments(
        lsInput: LSInput,
    )
    case Quit()
    case Error()
}