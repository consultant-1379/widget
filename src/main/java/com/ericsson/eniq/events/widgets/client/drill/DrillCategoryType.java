/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.drill;

import java.util.List;

/**
 * @author ecarsea
 * @since 2012
 *
 */
public class DrillCategoryType {

    public static enum DrillCriteriaType {
        EXCLUDE("exclude"), INCLUDE("include");

        private final String type;

        DrillCriteriaType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }

        public static DrillCriteriaType fromString(String type) {
            for (DrillCriteriaType dct : DrillCriteriaType.values()) {
                if (dct.toString().equalsIgnoreCase(type)) {
                    return dct;
                }
            }
            return INCLUDE;
        }
    }

    private final String id;

    private final String name;

    private final DrillCriteria criteria;

    public DrillCategoryType(String id) {
        this(id, "");
    }

    public DrillCategoryType(String id, String name) {
        this(id, name, null);
    }

    /**
     * @param id
     * @param name
     * @param criteria
     */
    public DrillCategoryType(String id, String name, DrillCriteria criteria) {
        super();
        this.id = id;
        this.name = name;
        this.criteria = criteria;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the criteria
     */
    public DrillCriteria getCriteria() {
        return criteria;
    }

    public static class DrillCriteria {
        private final DrillCriteriaType criteriaType;

        private final List<SeriesMatcherType> seriesMatchers;

        /**
         * @param criteriaType
         */
        public DrillCriteria(DrillCriteriaType criteriaType, List<SeriesMatcherType> seriesMatchers) {
            super();
            this.criteriaType = criteriaType;
            this.seriesMatchers = seriesMatchers;
        }

        /**
         * @return the criteriaType
         */
        public DrillCriteriaType getCriteriaType() {
            return criteriaType;
        }

        /**
         * @return the seriesMatchers
         */
        public List<SeriesMatcherType> getSeriesMatchers() {
            return seriesMatchers;
        }

    }

    public static class SeriesMatcherType {
        private final String seriesId;

        private final List<String> values;

        /**
         * @param seriesId
         * @param values
         */
        public SeriesMatcherType(String seriesId, List<String> values) {
            super();
            this.seriesId = seriesId;
            this.values = values;
        }

        /**
         * @return the seriesId
         */
        public String getSeriesId() {
            return seriesId;
        }

        /**
         * @return the values
         */
        public List<String> getValues() {
            return values;
        }
    }
}
