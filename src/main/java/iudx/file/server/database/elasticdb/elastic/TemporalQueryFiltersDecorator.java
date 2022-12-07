package iudx.file.server.database.elasticdb.elastic;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import static iudx.file.server.database.elasticdb.utilities.Constants.*;

public class TemporalQueryFiltersDecorator implements ElasticsearchQueryDecorator{
    private static final Logger LOGGER = LogManager.getLogger(TemporalQueryFiltersDecorator.class);
    private Map<FilterType, List<Query>> queryFilters;
    private JsonObject requestQuery;

    public TemporalQueryFiltersDecorator(Map<FilterType, List<Query>> queryFilters,
                                         JsonObject requestQuery) {
        this.queryFilters = queryFilters;
        this.requestQuery = requestQuery;
    }
    @Override
    public Map<FilterType, List<Query>> add() {
        final String time = requestQuery.getString(TIME);
        final String endTime = requestQuery.getString(END_TIME);
        Query temporalQuery = RangeQuery
                .of(r -> r
                        .field(TIME_RANGE)
                        /*.lte(JsonData.of(endTime))*/
                        /*.gte(JsonData.of(time))*/
                        .from(time)
                        .to(endTime))
                ._toQuery();

        LOGGER.debug("TQF in line 36 = "+ temporalQuery);
        List<Query> queryList = queryFilters.get(FilterType.FILTER);
        queryList.add(temporalQuery);
        return queryFilters;
    }

   /* @Override
    public BoolQueryBuilder parse(BoolQueryBuilder builder, JsonObject json) {
        builder
                .filter(QueryBuilders.rangeQuery(TIME_RANGE)
                        .lte(json.getString(END_TIME))
                        .gte(json.getString(TIME)));
        LOGGER.debug("TQF in line 36 = "+ builder);
        return builder;
    }*/
}
