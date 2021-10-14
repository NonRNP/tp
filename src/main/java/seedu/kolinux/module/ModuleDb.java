package seedu.kolinux.module;

import java.util.HashMap;

/**
 * ModuleDb class contains a HashMap that associates every module's code (key) with its modular information (value).
 */
public class ModuleDb {
    private JsonReader jsonReader = new JsonReader();
    private HashMap<String, ModuleDetails> moduleDetailsMap = new HashMap<>();

    /**
     * Loads module information stored in a JSON file into a HashMap.
     */
    public void initModuleDb() {
        assert moduleDetailsMap.isEmpty() : "moduleDetailsMap is already initialized";
        moduleDetailsMap = jsonReader.readModuleDb();
    }

    public ModuleDetails getModuleInfo(String code) {
        return moduleDetailsMap.get(code);
    }
}
