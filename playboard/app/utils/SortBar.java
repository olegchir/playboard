package utils;

import play.modules.paginate.ModelPaginator;

/**
 * User: olegchir
 */
public class SortBar {
    public static final String DEFAULT_SORT_BY_DATE_CAPTION = "by date";
    public static final String DEFAULT_SORT_BY_ADVERTISER_CAPTION = "by advertiser";
    public static final String SQL_ASCENDING_ORDER = "asc";
    public static final String SQL_DESCENDING_ORDER = "desc";
    public static final String SORT_ENABLED_INDICATOR = "true";
    public static final String SORT_DISABLED_INDICATOR = "false";
    public static final String SORT_BY_DATE_MAPPING = "date";
    public static final String SORT_BY_ADVERTISER_MAPPING = "advertiser";
    public static final String SORT_BY_ADVERTISER_KEY_FIELD = "author.fullname";
    public static final String SORT_BY_DATE_KEY_FIELD = "postedAt";

    private String sortByDateCaption = DEFAULT_SORT_BY_DATE_CAPTION;
    private String sortByAdvertiserCaption = DEFAULT_SORT_BY_ADVERTISER_CAPTION;
    private ModelPaginator advertsFound;
    private String sortingReversed;
    private String lastSortBy;
    private String sortBy;

    /**
     * Create pseudographic caption from base caption based on selection and sorting stats
     * @param baseCaption
     * @param activeMenuItem
     * @param asc ascending or descending order of sorting that is currently set
     * @return
     */
    public static final String getCaption(String baseCaption, boolean activeMenuItem, boolean asc) {
        if (activeMenuItem) {
            String arrow = "";
            if (asc) {
                arrow = "↓";
            } else {
                arrow = "↑";
            }
            return "["+baseCaption+" "+arrow+"]";
        } else {
            return baseCaption;
        }
    }

    /**
     * Create sortbar, swap labels and sorting if it is appropriate. Use getters to get labels.
     * @param advertsFoundParam ModelPaginator with source list of items
     * @param sortingReversedParam sorting should be reversed or not - that parameter autoswapped if it is appropriate
     * @param sortByParam current selected sort-by field
     * @param lastSortByParam previous selected sort-by field
     */
    public SortBar(ModelPaginator advertsFoundParam, String sortingReversedParam, String sortByParam, String lastSortByParam) {
        this.advertsFound = advertsFoundParam;
        this.lastSortBy = lastSortByParam;
        this.sortBy = sortByParam;
        this.sortingReversed = sortingReversedParam;

        //Map string indicator to a boolean flag
        Boolean sortingReversedFlag = SORT_ENABLED_INDICATOR.equals(sortingReversed);

        //Reverse sorting if it is appropriate, set vals to boolean flag and string indicator
        //Please note that sorting order will be changed only if you click sort button twice
        if (null != lastSortBy && null != sortBy && sortBy.equals(lastSortBy)) {
            if (sortingReversedFlag) {
                this.sortingReversed = SORT_DISABLED_INDICATOR;
                sortingReversedFlag = false;
            } else {
                this.sortingReversed = SORT_ENABLED_INDICATOR;
                sortingReversedFlag = true;
            }
        }

        //Generate filter postfix based on sorting order
        String sortingReversedFilter = sortingReversedFlag?SQL_ASCENDING_ORDER:SQL_DESCENDING_ORDER;

        String filter = "";

        //Create filter based on sortBy field and generate related captions
        if (null!=sortBy && !"".equals(sortBy)) {
            if (SORT_BY_ADVERTISER_MAPPING.equals(sortBy)) {
                filter = SORT_BY_ADVERTISER_KEY_FIELD;
                sortByAdvertiserCaption = SortBar.getCaption(DEFAULT_SORT_BY_ADVERTISER_CAPTION,true,sortingReversedFlag);
                sortByDateCaption = SortBar.getCaption(DEFAULT_SORT_BY_DATE_CAPTION,false,sortingReversedFlag);
            } else if (SORT_BY_DATE_MAPPING.equals(sortBy)) {
                filter = SORT_BY_DATE_KEY_FIELD;
                sortByAdvertiserCaption = SortBar.getCaption(DEFAULT_SORT_BY_ADVERTISER_CAPTION,false,sortingReversedFlag);
                sortByDateCaption = SortBar.getCaption(DEFAULT_SORT_BY_DATE_CAPTION,true,sortingReversedFlag);
            }
            filter += (" "+sortingReversedFilter);
        }

        this.lastSortBy = sortBy;

        //If filter exists, apply postfix to it
        if (!"".equals(filter) && null!=filter ) {
            advertsFound = advertsFound.orderBy(filter);
        }

    }

    public String getSortByDateCaption() {
        return sortByDateCaption;
    }

    public void setSortByDateCaption(String sortByDateCaption) {
        this.sortByDateCaption = sortByDateCaption;
    }

    public String getSortByAdvertiserCaption() {
        return sortByAdvertiserCaption;
    }

    public void setSortByAdvertiserCaption(String sortByAdvertiserCaption) {
        this.sortByAdvertiserCaption = sortByAdvertiserCaption;
    }

    public ModelPaginator getAdvertsFound() {
        return advertsFound;
    }

    public void setAdvertsFound(ModelPaginator advertsFound) {
        this.advertsFound = advertsFound;
    }

    public String getSortingReversed() {
        return sortingReversed;
    }

    public void setSortingReversed(String sortingReversed) {
        this.sortingReversed = sortingReversed;
    }
}
