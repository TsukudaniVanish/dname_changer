package repository

import usecase as U 

class Repository extends U.Repository {
    def FindSegments(
        driveHandler: U.DriveHandler, 
        pageMax:Int,
        pageSize: Int,
        isFileOnly: Boolean,
        isDirOnly: Boolean,
        name: String,
    ): (Seq[U.File], String) = {
        consumeToken(driveHandler, pageSize, pageMax,List[U.File](), "", 0, isFileOnly, isDirOnly, name)        
    }

    private def consumeToken(
        driveHandler: U.DriveHandler, 
        pageSize: Int,
        pageMax: Int ,
        files:Seq[U.File], 
        nextPageToken:String, 
        pageNum: Int,
        isFileOnly: Boolean,
        isDirOnly: Boolean,
        name: String,
    ): (Seq[U.File], String) =
        if pageNum >= pageMax then {
            return (files, nextPageToken)
        } else {
            val (fs,token)  = driveHandler.FindFiles(pageSize, nextPageToken, isFileOnly, isDirOnly, name);
            if token.isEmpty() then 
                (files ++ fs, "")
            else 
                consumeToken(driveHandler, pageSize, pageMax,files ++ fs, token, pageNum + 1, isFileOnly, isDirOnly, name)
        }
}