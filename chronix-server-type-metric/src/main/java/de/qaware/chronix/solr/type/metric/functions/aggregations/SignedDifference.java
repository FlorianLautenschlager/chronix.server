/*
 * Copyright (C) 2016 QAware GmbH
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package de.qaware.chronix.solr.type.metric.functions.aggregations;

import de.qaware.chronix.server.functions.ChronixAggregation;
import de.qaware.chronix.server.functions.FunctionValueMap;
import de.qaware.chronix.timeseries.MetricTimeSeries;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * The signed difference (sdiff) aggregation returns the difference between the first and the last value.
 * It could be negative in that case, when the last value is below the first.
 *
 * @author f.lautenschlager
 */
public final class SignedDifference implements ChronixAggregation<MetricTimeSeries> {

    /**
     * Calculate the difference between the first and the last value of a given time series
     *
     * @param timeSeries the time series
     * @return the average or 0 if the list is empty
     */
    @Override
    public void execute(MetricTimeSeries timeSeries, FunctionValueMap functionValueMap) {
        //If it is empty, we return NaN
        if (timeSeries.size() <= 0) {
            functionValueMap.add(this, Double.NaN);
            return;
        }

        //we need to sort the time series
        timeSeries.sort();
        //get the first and the last value
        double first = timeSeries.getValue(0);
        double last = timeSeries.getValue(timeSeries.size() - 1);

        //both values are negative
        if (first < 0 && last < 0) {
            functionValueMap.add(this, last - first);
            return;
        }

        //both value are positive
        if (first > 0 && last > 0) {
            functionValueMap.add(this, last - first);
            return;
        }

        //start is negative and end is positive
        if (first < 0 && last > 0) {
            functionValueMap.add(this, last - first);
            return;
        }

        //start is positive and end is negative
        functionValueMap.add(this, last - first);
    }

    @Override
    public String getQueryName() {
        return "sdiff";
    }

    @Override
    public String getTimeSeriesType() {
        return "metric";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        return new EqualsBuilder()
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .toHashCode();
    }
}
