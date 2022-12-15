package repository

import usecase as U 

class Repository extends U.Repository {
    def FindFiles(driveHandler: U.DriveHandler, pageMax:Int = 100): (Seq[U.File], Option[String]) = {
        val pageSize = 20;
        consumeToken(driveHandler, pageSize, pageMax,List[U.File](), Some(""), 0)        
    }

    def consumeToken(driveHandler: U.DriveHandler, pageSize: Int,pageMax: Int ,files:Seq[U.File], nextPageToken:Option[String], pageNum: Int): (Seq[U.File], Option[String]) =
        if pageNum >= pageMax then {
            return (files, nextPageToken)
        } else nextPageToken match
            case None => (files, nextPageToken)
            case Some(value) => {
                val (fs,nextPageToken)  = driveHandler.FindFiles(pageSize, value);
                consumeToken(driveHandler, pageSize, pageMax,files ++ fs, nextPageToken, pageNum + 1)
            }
        
}