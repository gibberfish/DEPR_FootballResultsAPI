package io.swagger.parser.processors;

import io.swagger.models.Model;
import io.swagger.models.RefModel;
import io.swagger.models.Swagger;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.refs.RefFormat;
import io.swagger.parser.ResolverCache;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static io.swagger.parser.util.RefUtils.computeDefinitionName;
import static io.swagger.parser.util.RefUtils.isAnExternalRefFormat;

public final class ExternalRefProcessor {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ExternalRefProcessor.class);

    private final ResolverCache cache;
    private final Swagger swagger;

    public ExternalRefProcessor(ResolverCache cache, Swagger swagger) {
        this.cache = cache;
        this.swagger = swagger;
    }

    public String processRefToExternalDefinition(String $ref, RefFormat refFormat) {
        final Model model = cache.loadRef($ref, refFormat, Model.class);

        String newRef;

        Map<String, Model> definitions = swagger.getDefinitions();

        if (definitions == null) {
            definitions = new HashMap<>();
        }

        final String possiblyConflictingDefinitionName = computeDefinitionName($ref);

        final Model existingModel = definitions.get(possiblyConflictingDefinitionName);

        if (existingModel != null) {
            LOGGER.debug("A model for " + existingModel + " already exists");
        }
        newRef = possiblyConflictingDefinitionName;
        cache.putRenamedRef($ref, newRef);


        //If this is a new model, then check it for other sub references
        String file = $ref.split("#/")[0];
        if (model instanceof RefModel) {
            RefModel refModel = (RefModel) model;
            if(isAnExternalRefFormat(refModel.getRefFormat())) {
                refModel.set$ref(processRefToExternalDefinition(refModel.get$ref(), refModel.getRefFormat()));
            } else {
                processRefToExternalDefinition(file + refModel.get$ref(), RefFormat.RELATIVE);
            }
        }
        //Loop the properties and recursively call this method;
        Map<String, Property> subProps = model.getProperties();
        if(subProps != null) {
            for (Map.Entry<String, Property> prop : subProps.entrySet()) {
                if (prop.getValue() instanceof RefProperty) {
                    RefProperty subRef = (RefProperty) prop.getValue();

                    if(isAnExternalRefFormat(subRef.getRefFormat())) {
                        subRef.set$ref(processRefToExternalDefinition(subRef.get$ref(), subRef.getRefFormat()));
                    } else {
                        processRefToExternalDefinition(file + subRef.get$ref(), RefFormat.RELATIVE);
                    }
                } else if (prop.getValue() instanceof ArrayProperty) {
                    ArrayProperty arrayProperty = (ArrayProperty) prop.getValue();
                    PropertyProcessor propertyProcessor = new PropertyProcessor(cache, swagger);
                    propertyProcessor.processProperty(arrayProperty);
                }
            }
        }

        checkForNestedRefsInModel(model);

        if(existingModel == null) {
            // don't overwrite existing model reference
            swagger.addDefinition(newRef, model);
        }

        return newRef;
    }

    private void checkForNestedRefsInModel(Model model) {
        if (model == null || model.getProperties() == null) {
            return;
        }

        Map<String, Property> propertyMap = model.getProperties();
        resolveNestedProperties(propertyMap);
    }

    private void resolveNestedProperties(Map<String, Property> propertyMap) {
        if (propertyMap == null) {
            return;
        }
        for (Map.Entry<String, Property> entry : propertyMap.entrySet()) {
            Property property = entry.getValue();
            if (property instanceof RefProperty && isAnExternalRefFormat(((RefProperty) property).getRefFormat())) {
                RefProperty refProp = (RefProperty) property;
                processRefToExternalDefinition(refProp.get$ref(), refProp.getRefFormat());
            } else if (property instanceof ObjectProperty) {
                resolveNestedProperties(((ObjectProperty) property).getProperties());
            }
        }
    }
}
