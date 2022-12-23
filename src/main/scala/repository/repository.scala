package repository

import usecase as U 

class Repository(
    driveHandler: DriveHandler,
) extends U.Repository {
    def FindSegments(
        input: domain.LSInput,
    ): (Seq[U.File], String) = {
        consumeToken(driveHandler, input, "", 0, List[U.File]())        
    }

    private def consumeToken(
        driveHandler: DriveHandler, 
       input: domain.LSInput,
       nextPageToken: String,
       pageNum: Int,
       segments: Seq[U.File]
    ): (Seq[U.File], String) =
        if  pageNum >= input.PageNum then {
            return (segments, nextPageToken)
        } else {
            val (fs,token)  = driveHandler.FindFiles(input.PageSize, nextPageToken, input.IsFileOnly, input.IsDirOnly, input.Name, input.OptParentId);
            if token.isEmpty() then 
                return (segments ++ fs, "")
            else 
                consumeToken(driveHandler, input, token, pageNum + 1, segments ++ fs)
        }
}