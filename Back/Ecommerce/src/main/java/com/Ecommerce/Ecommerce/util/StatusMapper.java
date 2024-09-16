package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.StatusDTO;
import com.Ecommerce.Ecommerce.entity.Status;

public class StatusMapper {

    public static StatusDTO toDTO(Status status) {
        if (status == null) {
            return null;
        }

        return new StatusDTO(
                status.getId(),
                status.getNome(),
                status.getCodigo());
    }

    public static Status toEntity(StatusDTO dto) {
        if (dto == null) {
            return null;
        }

        Status status = new Status();
        status.setId(dto.getId());
        status.setNome(dto.getNome());
        status.setCodigo(dto.getCodigo());

        return status;
    }
}
