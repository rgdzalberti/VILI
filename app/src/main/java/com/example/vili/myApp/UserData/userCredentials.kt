package viliApp

class userCredentials(){

    companion object{

        private var email = ""
        private var password = ""

        public fun setUserCredentials(email: String, password: String ){
            this.email = email
            this.password = password

        }

        public fun tryConnection(email: String, password: String ):Boolean{
            var boolReturn = false //No se ha podido conectar a la BBDD
            //Intentar conexion a la BBDD con email y pass

            //if valid setUserCredentials()
            //else throw non valid

            return boolReturn
        }

        public fun registerCredentials(email: String, password: String ){
            //if no errors entonces setUserCredentials
        }


    }



}

