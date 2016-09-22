package io.swagger.codegen.languages;

import io.swagger.codegen.SupportingFile;

import java.io.File;

public class KFSpringCodegen extends SpringCodegen {

    public static final String API_MOCK_PREFIX = "mockApi";
    public static final String PACKAGE_DELIMITER = ".";

    @Override
    public void processOpts() {
        super.processOpts();

        if (!this.interfaceOnly) {
            if (library.equals(DEFAULT_LIBRARY)) {

                supportingFiles.add(new SupportingFile("api-config-example.mustache",
                        "", "api-config-example.json"));

                apiTemplateFiles.put("mockApiConfigurationJson.mustache", processPath("api-config", "json"));
                apiTemplateFiles.put("apiService.mustache", "Service.java");
                apiTemplateFiles.put("apiServiceImpl.mustache", "ServiceImpl.java");
            }
        }
    }

    private String processPath(String s, String suffix) {
        return (outputFolder() + PACKAGE_DELIMITER + s).replace(PACKAGE_DELIMITER, java.io.File.separator) + "." + suffix;
    }

    @Override
    public String apiFilename(String templateName, String tag) {
        if (!templateName.startsWith(API_MOCK_PREFIX)) {
            return super.apiFilename(templateName, tag);
        }

        return apiTemplateFiles().get(templateName).replace(".json",  "_" + title + ".json");
    }

    @Override
    public String getName() {
        return "kf-spring";
    }
}
