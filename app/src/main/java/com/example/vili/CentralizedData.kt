package viliApp

class CentralizedData {

    companion object{

        var gameID:String = ""

        //Cuando elimino un juego de la BBDD me comunico con la UI
        //Con esta variable que será observada
        var recomposeUI: Boolean = false
        var gameIDtoRemove = ""

        public fun updateGameID(newGameID:String){
            gameID = newGameID;
        }

        @JvmName("getGameID1")
        public fun getGameID():String{
            return gameID
        }

        fun updateRecomposeList(){
            /*
            if (recomposeUI == false) {
                gameIDtoRemove = ""}

             */
            recomposeUI = !recomposeUI
        }


        //CONVERTIR A GAMEUSERUNION
        fun convertToGameUserUnion(gameList :List<Game>,userGameList :List<UserGameEntry>):List<GameUserUnion>{

            val unifiedList = mutableListOf<GameUserUnion>()

            for (i in 0 until gameList.size){
                unifiedList.add(GameUserUnion(userGameList[i].gameID,userGameList[i].score,userGameList[i].comment,gameList[i].name,gameList[i].imageURL))
            }


            return unifiedList

        }


    }

}