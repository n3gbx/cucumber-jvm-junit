package com.aqa.example.parameters;

import com.aqa.example.domain.enums.PassengerType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterByTypeTransformer;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableCellByTypeTransformer;
import io.cucumber.datatable.TableCellTransformer;
import io.cucumber.datatable.TableEntryByTypeTransformer;

import java.lang.reflect.Type;
import java.util.Map;

public class ParameterTypes implements TypeRegistryConfigurer {

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineDataTableType(new DataTableType(PassengerType.class, (TableCellTransformer<PassengerType>) cell -> {
            return PassengerType.valueOf(cell.toUpperCase());
        }));

        Transformer transformer = new Transformer();
        typeRegistry.setDefaultDataTableCellTransformer(transformer);
        typeRegistry.setDefaultDataTableEntryTransformer(transformer);
        typeRegistry.setDefaultParameterTransformer(transformer);
    }

    private class Transformer implements ParameterByTypeTransformer, TableEntryByTypeTransformer, TableCellByTypeTransformer {
        ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        @Override
        public Object transform(String s, Type type) {
            return objectMapper.convertValue(s, objectMapper.constructType(type));
        }

        @Override
        public Object transform(Map<String, String> entryValue, Type toValueType, TableCellByTypeTransformer cellTransformer) {
            return objectMapper.convertValue(entryValue, objectMapper.constructType(toValueType));
        }
    }
}
