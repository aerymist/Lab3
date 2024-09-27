package org.translation;

import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {
        Translator translator = new JSONTranslator(null);
        // Translator translator = new InLabByHandTranslator();

        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        while (true) {
            String quit = "quit";
            String country = promptForCountry(translator);
            // CheckStyle: The String "quit" appears 3 times in the file.
            // Checkstyle: String literal expressions should be on the left side of an equals comparison
            if ("quit".equals(country)) {
                break;
            }
            // Task: Once you switch promptForCountry so that it returns the country
            //            name rather than the 3-letter country code, you will need to
            //            convert it back to its 3-letter country code when calling promptForLanguage
            CountryCodeConverter ccc = new CountryCodeConverter();
            LanguageCodeConverter lcc = new LanguageCodeConverter();
            String language = promptForLanguage(translator, ccc.fromCountry(country));
            if (language.equals(quit)) {
                break;
            }
            // Task: Once you switch promptForLanguage so that it returns the language
            //            name rather than the 2-letter language code, you will need to
            //            convert it back to its 2-letter language code when calling translate.
            //            Note: you should use the actual names in the message printed below though,
            //            since the user will see the displayed message.
            System.out.println(country + " in " + language + " is "
                    + translator.translate(country, lcc.fromLanguage(language)));
            System.out.println("Press enter to continue or quit to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if (quit.equals(textTyped)) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator) {
        List<String> countries = translator.getCountries();
        // convert to names
        CountryCodeConverter ccc = new CountryCodeConverter();
        List<String> fullCountries = new java.util.ArrayList<>();
        for (String countryCode : countries) {
            String countryName = ccc.fromCountryCode(countryCode);
            fullCountries.add(countryName);
        }
        // sort countries alphabetically
        java.util.Collections.sort(fullCountries);

        for (String fullCountry : fullCountries) {
            System.out.println(fullCountry);
        }

        System.out.println("select a country from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator,
                                            String country) {
        List<String> languages = translator.getCountryLanguages(country);
        LanguageCodeConverter lcc = new LanguageCodeConverter();
        java.util.ArrayList<String> languageNames = new java.util.ArrayList<String>();
        for (String languageCode : languages) {
            languageNames.add(lcc.fromLanguageCode(languageCode));
        }
        java.util.Collections.sort(languageNames);

        for (String fullName: languageNames) {
            System.out.println(fullName);
        }

        System.out.println("select a language from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
