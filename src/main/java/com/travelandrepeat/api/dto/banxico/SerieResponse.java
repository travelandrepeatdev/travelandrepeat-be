package com.travelandrepeat.api.dto.banxico;

import java.util.List;

public record SerieResponse (String idSerie, String titulo, List<DataSerieResponse> datos) {
}
