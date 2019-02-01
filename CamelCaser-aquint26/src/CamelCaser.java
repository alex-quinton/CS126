public class CamelCaser {
    public static String camelCase(String inputString){

        //first tests for illegal characters and formatting
        try{

            if(inputString == null){
                throw new IllegalArgumentException("Null Input");
            }

            for(int i = 0; i < inputString.length(); i++){
                char c = inputString.charAt(i); //character being checked at the moment

                // checks to see if first character is a digit
                if(i == 0 && (c > 47 && c < 58)){
                    throw new IllegalArgumentException(ErrorConstants.INVALID_FORMAT);
                }

                if(!(
                        (c == ' ' || c == '\n' || c == '\t') //character is whitespace
                        || (c > 47 && c < 58) //character is digit
                        || (c > 64 && c < 91) //character is uppercase letter
                        || (c > 96 && c < 123) // character is lowercase letter
                 )){
                        throw new IllegalArgumentException(ErrorConstants.INVALID_CHARACTER);
                }
            }
        } catch(IllegalArgumentException e){
            return "Exception occurred"; //output so that assertEquals tests can tell if there was an error
        }

        String[] inputWords = inputString.toLowerCase().split("[ \n\t]");
        String outputString = "";

        //iterates through list of words in inputString
        for(int i = 0; i < inputWords.length; i++){
            if (i == 0){
                //first word is lower case
                outputString += inputWords[i];
            } else {
                //capitalizes first letter, leaves rest as lower case
                outputString += inputWords[i].substring(0,1).toUpperCase();
                outputString += inputWords[i].substring(1);
            }
        }

        return outputString;
    }
}
