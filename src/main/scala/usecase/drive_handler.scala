package usecase


/**
 * Google Drive Api Handler 
 *  @throws IOException If the credentials.json file cannot be found.
 */
trait DriveHandler {
    def GetMaxPageSize(): Int 
    /**
      * 
      *
      * @param pageSize
      * @param nextPageToken
      * @return files, nextPageToken
      */
    def FindFiles(
      pageSize: Int, 
      nextPageToken:String,
      isFileOnly: Boolean,
      isFolderOnly: Boolean,
      name: String
    ): (Seq[File], String)
}