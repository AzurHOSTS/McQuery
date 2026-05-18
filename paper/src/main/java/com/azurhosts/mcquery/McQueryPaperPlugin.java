package com.azurhosts.mcquery;

public class McQueryPaperPlugin extends Bootstrap{

    @Override
    protected void onStart() {
        saveDefaultConfig();
    }

    @Override
    protected void onStop() {
        saveConfig();
    }
}
