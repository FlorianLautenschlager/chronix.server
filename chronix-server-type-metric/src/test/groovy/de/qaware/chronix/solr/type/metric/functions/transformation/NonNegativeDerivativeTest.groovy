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
package de.qaware.chronix.solr.type.metric.functions.transformation

import de.qaware.chronix.server.functions.FunctionValueMap
import de.qaware.chronix.timeseries.MetricTimeSeries
import spock.lang.Specification

import java.time.Instant

/**
 * Unit test for the non negative derivative transformation
 * @author f.lautenschlager
 */
class NonNegativeDerivativeTest extends Specification {
    def "test transform"() {
        given:
        def timeSeriesBuilder = new MetricTimeSeries.Builder("Derivative time series","metric")
        def derivative = new NonNegativeDerivative()

        timeSeriesBuilder.point(dateOf("2016-05-23T10:51:00.000Z"), 5)
        timeSeriesBuilder.point(dateOf("2016-05-23T10:51:01.000Z"), 4)

        timeSeriesBuilder.point(dateOf("2016-05-23T10:51:06.500Z"), 6)
        timeSeriesBuilder.point(dateOf("2016-05-23T10:51:07.000Z"), 10)
        timeSeriesBuilder.point(dateOf("2016-05-23T10:51:08.000Z"), 31)
        timeSeriesBuilder.point(dateOf("2016-05-23T10:51:09.000Z"), 9)
        timeSeriesBuilder.point(dateOf("2016-05-23T10:51:10.000Z"), 2)

        timeSeriesBuilder.point(dateOf("2016-05-23T10:51:15.000Z"), 1)
        timeSeriesBuilder.point(dateOf("2016-05-23T10:51:16.000Z"), 5)

        def timeSeries = timeSeriesBuilder.build()
        def analysisResult = new FunctionValueMap(1, 1, 1)

        when:
        derivative.execute(timeSeries, analysisResult)

        then:
        timeSeries.size() == 4
    }

    long dateOf(format) {
        Instant.parse(format as String).toEpochMilli()
    }

    def "test getType"() {
        expect:
        new NonNegativeDerivative().getQueryName() == "nnderivative"
    }

    def "test getArguments"() {
        expect:
        new NonNegativeDerivative().getArguments().length == 0
    }

    def "test equals and hash code"() {
        expect:
        def function = new NonNegativeDerivative()
        !function.equals(null)
        !function.equals(new Object())
        function.equals(function)
        function.equals(new NonNegativeDerivative())
        new NonNegativeDerivative().hashCode() == new NonNegativeDerivative().hashCode()
    }
}
