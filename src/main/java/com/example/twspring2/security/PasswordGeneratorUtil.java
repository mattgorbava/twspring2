package com.example.twspring2.security;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

public class PasswordGeneratorUtil {
    public static String generatePassword() {
        PasswordGenerator gen = new PasswordGenerator();

        EnglishCharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(1);

        EnglishCharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(1);

        EnglishCharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(1);

        EnglishCharacterData special = EnglishCharacterData.SpecialAscii;
        CharacterRule specialRule = new CharacterRule(special);
        specialRule.setNumberOfCharacters(1);

        String password = gen.generatePassword(8, specialRule, lowerCaseRule, upperCaseRule, digitRule);

        return password;
    }
}
