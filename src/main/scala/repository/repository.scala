package repository

import usecase as U 

class Repository extends U.Repository {
    def FindSegments(
        driveHandler: U.DriveHandler, 
        pageMax:Int,
        pageSize: Int,
        isFileOnly: Boolean,
        isDirOnly: Boolean,
    ): (Seq[U.File], Option[String]) = {
        consumeToken(driveHandler, pageSize, pageMax,List[U.File](), Some(""), 0, isFileOnly, isDirOnly)        
    }

    private def consumeToken(
        driveHandler: U.DriveHandler, 
        pageSize: Int,
        pageMax: Int ,
        files:Seq[U.File], 
        nextPageToken:Option[String], 
        pageNum: Int,
        isFileOnly: Boolean,
        isDirOnly: Boolean,
    ): (Seq[U.File], Option[String]) =
        if pageNum >= pageMax then {
            return (files, nextPageToken)
        } else nextPageToken match
            case None => (files, nextPageToken)
            case Some(value) => {
                val (fs,nextPageToken)  = driveHandler.FindFiles(pageSize, value, isFileOnly, isDirOnly);
                consumeToken(driveHandler, pageSize, pageMax,files ++ fs, nextPageToken, pageNum + 1, isFileOnly, isDirOnly)
            }
        
}