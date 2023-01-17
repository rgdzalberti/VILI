package viliApp

class CentralizedData {

    companion object{

        var gameID:String = ""

        public fun updateGameID(newGameID:String){
            gameID = newGameID;
        }

        @JvmName("getGameID1")
        public fun getGameID():String{
            return gameID
        }


    }

}