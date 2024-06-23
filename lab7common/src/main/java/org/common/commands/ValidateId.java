package org.common.commands;

import org.common.managers.Collection;
import org.common.utility.InvalidFormatException;
import org.common.utility.*;


/**
 * A class for checking element IDs
 */
public class ValidateId {
    public static Long validateId(String idStr, boolean mustBeUnique, Collection collection){
        if (!Validator.validate(idStr, TypesOfArgs.Long,false)){
            throw new InvalidFormatException("ErrorIdMustBePositive");
        }
        Long id = Long.parseLong(idStr);
        if (id<=0){
            throw new InvalidFormatException("ErrorIdMustBePositive");
        }
        String not = mustBeUnique ? "" :"Not";
        if (mustBeUnique == collection.getHashMap().containsKey(id)) {
            throw new InvalidFormatException("ErrorIdMustBe"+not +"Unique");
        }
        return Long.parseLong(idStr);
    }
}
