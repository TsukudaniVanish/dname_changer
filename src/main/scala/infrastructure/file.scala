package infrastructure

import com.google.api.services.drive.model as DriveModel
import usecase as U

class File(metaFile: DriveModel.File) extends U.File{
    def getName():String = metaFile.getName()
    def getID():String = metaFile.getId()
}