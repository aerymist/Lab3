package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    Map<String, String> countryToCode= new HashMap<String, String>();
    Map <String, HashMap <String, String>> codeToLanguages = new HashMap<>();


    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String countryName = jsonObject.getString("en");
                String countryCode = jsonObject.getString("alpha3");

                Map<String, String> langTranslation = new HashMap<>();
                countryToCode.put(countryName, countryCode);

                JSONArray langName = jsonObject.names();
                for (int j = 0; j < langName.length(); j++){
                    if (!langName.getString(j).equals("id") && !langName.getString(j).equals("alpha2") && !langName.getString(j).equals("alpha3")){
                        langTranslation.put(langName.getString(j), jsonObject.getString(langName.getString(j)));
                    }
                }

                codeToLanguages.put(countryCode, langTranslation);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {

        Map<String, String> languages = codeToLanguages.get(country);
        List<String> newLangs = new ArrayList<>();
        newLangs.addAll(languages.keySet());

        return newLangs;
    }

    @Override
    public List<String> getCountries() {

        ArrayList<String> countries = new ArrayList<>();

        countries.addAll(codeToLanguages.keySet());
        return countries;
    }

    @Override
    public String translate(String country, String language) {

        Map <String, String> languages = codeToLanguages.get(country);
        for (String lang: languages.keySet()){
            if (lang.equals(language)){
                return languages.get(lang);
            }
        }

        return null;
    }
}
