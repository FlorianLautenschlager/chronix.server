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
package de.qaware.chronix.solr.query.analysis.functions.aggregations;

import de.qaware.chronix.solr.query.analysis.functions.AnalysisType;
import de.qaware.chronix.solr.query.analysis.functions.ChronixAnalysis;
import de.qaware.chronix.timeseries.MetricTimeSeries;

/**
 * The maximum aggregation
 *
 * @author f.lautenschlager
 */
public final class Max implements ChronixAnalysis {

    /**
     * Calculates the maximum value of the first time series.
     *
     * @param args the time series
     * @return the maximum or 0 if the list is empty
     */
    @Override
    public double execute(MetricTimeSeries... args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Max aggregation needs at least one time series");
        }

        MetricTimeSeries timeSeries = args[0];
        double max = 0;

        if (timeSeries.size() <= 0) {
            return max;
        }
        int size = timeSeries.size();
        double[] values = timeSeries.getValues().toArray();

        for (int i = 0; i < size; i++) {
            double next = values[i];
            if (max < next) {
                max = next;
            }
        }
        return max;
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public AnalysisType getType() {
        return AnalysisType.MAX;
    }

    @Override
    public boolean needSubquery() {
        return false;
    }

    @Override
    public String getSubquery() {
        return null;
    }


}