package com.ostia.productcatalogservice.assembler;

import com.ostia.productcatalogservice.controller.CategoryController;
import com.ostia.productcatalogservice.dto.CategoryDTO;
import com.ostia.productcatalogservice.dto.UpdateCategoryDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static com.ostia.productcatalogservice.common.LinkRelation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoryModelAssembler implements RepresentationModelAssembler<CategoryDTO, EntityModel<CategoryDTO>> {

    @Override
    public EntityModel<CategoryDTO> toModel(CategoryDTO categoryDTO) {
        return EntityModel.of(
                categoryDTO,
                linkTo(methodOn(CategoryController.class).getCategory(categoryDTO.name())).withSelfRel(),
                linkTo(methodOn(CategoryController.class).deleteCategory(categoryDTO.name())).withRel(DELETE.rel()),
                linkTo(methodOn(CategoryController.class).updateCategory(categoryDTO.name(), new UpdateCategoryDTO(categoryDTO.description()))).withRel(UPDATE.rel())
        );
    }
}
