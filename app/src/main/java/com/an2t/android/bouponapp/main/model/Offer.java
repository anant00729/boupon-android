package com.an2t.android.bouponapp.main.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anantawasthy on 8/24/17.
 */

public class Offer {

    private String limit;
    private String page;
    private String keys;
    private Search search;

    public Offer(String limit, String page, String keys, Search search) {
        this.setLimit(limit);
        this.setPage(page);
        this.setKeys(keys);
        this.setSearch(search);

    }



    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }


    public static class Search{
        private String category;
        private boolean isFreeDeal;
        private boolean isFeatured;


        public Search(boolean isFeatured) {
            this.isFeatured = isFeatured;
        }

        public boolean isFreeDeal() {
            return isFreeDeal;
        }

        public void setFreeDeal(boolean freeDeal) {
            isFreeDeal = freeDeal;
        }

        public Search(String category, boolean isFreeDeal) {
            this.setCategory(category);
            this.setFreeDeal(isFreeDeal);
        }

        public Search(String category, boolean isFreeDeal, boolean isFeatured) {
            this.category = category;
            this.isFreeDeal = isFreeDeal;
            this.isFeatured = isFeatured;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    public class OfferRes{
        @SerializedName("data")
        @Expose
        private ArrayList<Data> data = null;
        @SerializedName("pages")
        @Expose
        private Pages pages;
        @SerializedName("items")
        @Expose
        private Items items;

        public ArrayList<Data> getData() {
            return data;
        }

        public void setData(ArrayList<Data> data) {
            this.data = data;
        }

        public Pages getPages() {
            return pages;
        }

        public void setPages(Pages pages) {
            this.pages = pages;
        }

        public Items getItems() {
            return items;
        }

        public void setItems(Items items) {
            this.items = items;
        }
    }

    public static class Data{

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("mainImage")
        @Expose
        private String mainImage;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("images")
        @Expose
        private List<String> images = null;
        @SerializedName("expiry")
        @Expose
        private String expiry;
        @SerializedName("start")
        @Expose
        private String start;
        @SerializedName("quantity")
        @Expose
        private Integer quantity;

        @SerializedName("isFreeDeal")
        @Expose
        private boolean isFreeDeal;



        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMainImage() {
            return mainImage;
        }

        public void setMainImage(String mainImage) {
            this.mainImage = mainImage;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getExpiry() {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public boolean isFreeDeal() {
            return isFreeDeal;
        }

        public void setFreeDeal(boolean freeDeal) {
            isFreeDeal = freeDeal;
        }



    }

    public class Pages{
        @SerializedName("current")
        @Expose
        private Integer current;
        @SerializedName("prev")
        @Expose
        private Integer prev;
        @SerializedName("hasPrev")
        @Expose
        private Boolean hasPrev;
        @SerializedName("next")
        @Expose
        private Integer next;
        @SerializedName("hasNext")
        @Expose
        private Boolean hasNext;
        @SerializedName("total")
        @Expose
        private Integer total;

        public Integer getCurrent() {
            return current;
        }

        public void setCurrent(Integer current) {
            this.current = current;
        }

        public Integer getPrev() {
            return prev;
        }

        public void setPrev(Integer prev) {
            this.prev = prev;
        }

        public Boolean getHasPrev() {
            return hasPrev;
        }

        public void setHasPrev(Boolean hasPrev) {
            this.hasPrev = hasPrev;
        }

        public Integer getNext() {
            return next;
        }

        public void setNext(Integer next) {
            this.next = next;
        }

        public Boolean getHasNext() {
            return hasNext;
        }

        public void setHasNext(Boolean hasNext) {
            this.hasNext = hasNext;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

    }

    public class Items{
        @SerializedName("begin")
        @Expose
        private Integer begin;
        @SerializedName("end")
        @Expose
        private Integer end;
        @SerializedName("total")
        @Expose
        private Integer total;

        public Integer getBegin() {
            return begin;
        }

        public void setBegin(Integer begin) {
            this.begin = begin;
        }

        public Integer getEnd() {
            return end;
        }

        public void setEnd(Integer end) {
            this.end = end;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }
    }
}
