package com.vector.studynews.view.emojicon;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.vector.studynews.R;
import com.vector.studynews.adapter.EmojiconGridAdapter;
import com.vector.studynews.adapter.EmojiconPagerAdapter;
import com.vector.studynews.entity.Emojicon;
import com.vector.studynews.entity.EmojiconGroupEntity;
import com.vector.studynews.utils.SmileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/8/21.
 */
public class EmojiconPagerView extends ViewPager {
    private static final String TAG = "EmojiconPagerView";
    private Context context;
    private List<EmojiconGroupEntity> groupEntities;
    private List<Emojicon> totalEmojiconList = new ArrayList<Emojicon>();
    private PagerAdapter pagerAdapter;
    private EmojiconPagerViewListener pagerViewListener;
    private int emojiconRows = 3;
    private int emojiconColumns = 7;

    private int bigEmojiconRows = 2;
    private int bigEmojiconColumns = 4;

    private int firstGroupPageSize;
    private int maxPageCount;
    private int previousPagerPosition;
    private List<View> viewpages;

    private EmojiconPagerViewListener emojiconPagerViewListener;
    public EmojiconPagerView(Context context) {
        this(context,null);
    }

    public EmojiconPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void init(List<EmojiconGroupEntity> emojiconGroupEntityList,int emojiconColumns,int bigEmojiconColumns){
        if(emojiconGroupEntityList==null){
            throw new RuntimeException("emojiconGroupList is null");
        }
        this.groupEntities = emojiconGroupEntityList;
        this.emojiconColumns = emojiconColumns;
        this.bigEmojiconColumns = bigEmojiconColumns;

        viewpages = new ArrayList<View>();
        for(int i = 0;i<groupEntities.size();i++){
            EmojiconGroupEntity group = groupEntities.get(i);
            List<Emojicon> groupEmojicons = group.getEmojiconList();
            totalEmojiconList.addAll(groupEmojicons);
            List<View> gridViews = getGroupGridViews(group);
            if(i==0){
                firstGroupPageSize = gridViews.size();
            }
            maxPageCount = Math.max(gridViews.size(),maxPageCount);
            viewpages.addAll(gridViews);
        }

        pagerAdapter = new EmojiconPagerAdapter(viewpages);
        setAdapter(pagerAdapter);
        //setOnPageChangeListener(new EmojiconPagerChangeListener());
        //if(emojiconPagerViewListener!=null){
//            emojiconPagerViewListener.onPagerViewInited(maxPageCount,firstGroupPageSize);
//        }
    }

    public List<View> getGroupGridViews(EmojiconGroupEntity groupEntity){
        List<Emojicon> emojiconList = groupEntity.getEmojiconList();
        int itemSize = emojiconColumns*emojiconRows-1;
        int totalSize = emojiconList.size();
        Emojicon.Type emojiType = groupEntity.getType();
        if(emojiType == Emojicon.Type.BIG_EXPRESSION){
            itemSize = bigEmojiconColumns * bigEmojiconRows;
        }
        int pageSize = totalSize%itemSize ==0 ?totalSize/itemSize:totalSize/itemSize+1;
        List<View> views = new ArrayList<View>();
        for(int i = 0;i<pageSize;i++){
            View view = View.inflate(context, R.layout.expression_gridview,this);
            GridView gv = (GridView) view.findViewById(R.id.grid_view);
            if(emojiType == Emojicon.Type.BIG_EXPRESSION){
                gv.setNumColumns(bigEmojiconColumns);
            }else{
                gv.setNumColumns(emojiconColumns);
            }
            List<Emojicon> list = new ArrayList<Emojicon>();
            if(i!=pageSize-1){
                list.addAll(emojiconList.subList(i*itemSize,(i+1)*itemSize));
            }else{
                list.addAll(emojiconList.subList(i*itemSize,totalSize));
            }
            if(emojiType != Emojicon.Type.BIG_EXPRESSION){
                Emojicon deleteIcon = new Emojicon();
                deleteIcon.setEmojiText(SmileUtils.DELETE_KEY);
                list.add(deleteIcon);
            }
            final EmojiconGridAdapter gridAdapter = new EmojiconGridAdapter(context,1,list,emojiType);
            gv.setAdapter(gridAdapter);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Emojicon emojicon = gridAdapter.getItem(position);
                    if(emojiconPagerViewListener!=null){
                        String emojiText = emojicon.getEmojiText();
                        if(emojiText!=null && emojiText.equals(SmileUtils.DELETE_KEY)){
                            emojiconPagerViewListener.onDeleteImageClicked();
                        }else{
                            emojiconPagerViewListener.onExpressionClicked(emojicon);
                        }
                    }
                }
            });
            views.add(view);
        }
        return views;
    }


    public void addEmojiconGroup(EmojiconGroupEntity groupEntity,boolean notifyDataChange){
        int pageSize = getPageSize(groupEntity);
        if(pageSize > maxPageCount){
            maxPageCount = pageSize;
            if(emojiconPagerViewListener!=null&&pagerAdapter!=null){
                emojiconPagerViewListener.onGroupMaxPageSizeChanged(maxPageCount);
            }
        }
        viewpages.addAll(getGroupGridViews(groupEntity));
        if(pagerAdapter!=null&&notifyDataChange){
            pagerAdapter.notifyDataSetChanged();
        }
    }

    public void removeEmojiconGroup(int position){
        if(position>groupEntities.size()-1){
            return;
        }
        if(pagerAdapter!=null){
            pagerAdapter.notifyDataSetChanged();
        }
    }
    private int getPageSize(EmojiconGroupEntity groupEntity){
        List<Emojicon> emojiconList = groupEntity.getEmojiconList();
        int itemSize = emojiconColumns * emojiconRows -1;
        int totalSize = emojiconList.size();
        Emojicon.Type emojiType = groupEntity.getType();
        if(emojiType== Emojicon.Type.BIG_EXPRESSION){
            itemSize = bigEmojiconColumns * bigEmojiconRows;
        }
        int pageSize = totalSize % itemSize == 0 ?totalSize/itemSize:totalSize/itemSize+1;
        return pageSize;
    }

    public void setGroupPosition(int position){
        if(getAdapter()!=null && position >=0 && position<groupEntities.size()){
            int count = 0;
            for(int i = 0;i<position;i++){
                count +=getPageSize(groupEntities.get(i));
            }
            setCurrentItem(count);
        }
    }

    private class EmojiconPagerChangeListener implements OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int endSize = 0;
            int groupPosition = 0;
            for(EmojiconGroupEntity groupEntity:groupEntities){
                int groupPageSize = getPageSize(groupEntity);
                if(endSize + groupPageSize>position){
                    if(previousPagerPosition-endSize<0){
                        if(emojiconPagerViewListener!=null){
                            emojiconPagerViewListener.onGroupPositionChanged(groupPosition,groupPageSize);
                            emojiconPagerViewListener.onGroupPagePositionChangedTo(0);
                        }
                        break;
                    }
                    if(previousPagerPosition-endSize>=groupPageSize){
                        if(emojiconPagerViewListener!=null){
                            emojiconPagerViewListener.onGroupPositionChanged(groupPosition, groupPageSize);
                            emojiconPagerViewListener.onGroupPagePositionChangedTo(position-endSize);
                        }
                        break;
                    }
                    if(emojiconPagerViewListener!=null){
                        emojiconPagerViewListener.onGroupInnerPagePositionChanged(previousPagerPosition-endSize,position-endSize);
                    }
                    break;
                }
                groupPosition++;
                endSize += groupPageSize;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void setEmojiconPageViewListener(EmojiconPagerViewListener emojiconPagerViewListener){
        this.emojiconPagerViewListener = emojiconPagerViewListener;
    }

    public interface EmojiconPagerViewListener{
        /**
         * pagerview initialized
         * @param groupMaxPageSize --max pages size
         * @param firstGroupPageSize-- size of first group pages
         */
        void onPagerViewInited(int groupMaxPageSize,int firstGroupPageSize);
        /**
         * group position changed
         * @param groupPosition--group position
         * @param pagerSizeOfGroup--page size of group
         */
        void onGroupPositionChanged(int groupPosition,int pagerSizeOfGroup);
        /**
         * page position changed
         * @param oldPosition
         * @param newPosition
         */
        void onGroupInnerPagePositionChanged(int oldPosition,int newPosition);
        /**
         * group page position changed
         * @param position
         */
        void onGroupPagePositionChangedTo(int position);
        /**
         * max page size changed
         * @param maxCount
         */
        void onGroupMaxPageSizeChanged(int maxCount);


        void onDeleteImageClicked();
        void onExpressionClicked(Emojicon emojicon);
    }
}
