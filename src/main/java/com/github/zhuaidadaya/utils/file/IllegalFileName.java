package com.github.zhuaidadaya.utils.file;

import java.util.Collection;
import java.util.LinkedHashSet;

public class IllegalFileName {
    private final Collection<String> illegals = new LinkedHashSet<>();
    private boolean illegal = false;

    public IllegalFileName(String name) {
        for(int c : name.chars().toArray()) {
            String each = String.valueOf((char) c);
            switch(each) {
                case "<", ">", "/", "\\", "|", ":", "\"", "*", "?" -> {
                    illegal = true;
                    illegals.add(each);
                }
            }
        }
    }

    public boolean isIllegal() {
        return illegal;
    }

    public Collection<String> getIllegalsCollection() {
        return illegals;
    }

    public String getIllegals() {
        StringBuilder illegals = new StringBuilder();
        int illegalCount = this.illegals.toArray().length;
        int split = 0;
        for(String s : this.illegals) {
            split++;
            illegals.append(s);
            if(illegalCount != split) {
                illegals.append(", ");
            }
        }
        return illegals.toString();
    }
}
