
        package ${basePackage}.service.impl;


        import ${basePackage}.dao.mapper.${modelNameUpperCamel}Mapper;
        import ${basePackage}.model.${modelNameUpperCamel};
        import ${basePackage}.model.${modelNameUpperCamel}Example;
        import org.yaukie.api.base.core.BaseService;
        import ${basePackage}.service.api.${modelNameUpperCamel}Service;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;



        /**
        * @author: ${author}
        * @create: ${date}
        **/
        @Service
        @Transactional
        public class ${modelNameUpperCamel}ServiceImpl extends BaseService<${modelNameUpperCamel}Mapper,${modelNameUpperCamel},${modelNameUpperCamel}Example> implements ${modelNameUpperCamel}Service {

        @Autowired
        private ${modelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;

        }
