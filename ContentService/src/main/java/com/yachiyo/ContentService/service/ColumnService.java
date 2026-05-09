package com.yachiyo.ContentService.service;

import com.yachiyo.ContentService.dto.*;
import com.yachiyo.ContentService.result.Result;

import java.util.List;

public interface ColumnService {

    Result<List<ColumnResponse>> searchColumn(SearchRequest searchRequest);

    Result<InteractionResponse> getInteraction(Long columnId);

    Result<Boolean> interactionColumn(InteractionRequest interactionRequest);

    Result<Boolean> addColumn(AddColumnRequest addColumnRequest);

    Result<Boolean> deleteColumn(Long id);
}
