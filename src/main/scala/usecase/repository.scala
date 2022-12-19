package usecase

trait Repository {
    /**
      * 
      *
      * @return files, nextPageToken
      */
    def FindSegments(
        driveHandler: DriveHandler, 
        pageMax: Int, 
        pageSize:Int,
        isFileOnly: Boolean,
        isDirOnly: Boolean,
        name:String,
    ): (Seq[File], String)
}