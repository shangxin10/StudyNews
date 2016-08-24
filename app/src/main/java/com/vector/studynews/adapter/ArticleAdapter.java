package com.vector.studynews.adapter;

import android.content.Context;
import android.widget.ListView;

import com.vector.studynews.R;
import com.vector.studynews.entity.Article;
import com.vector.studynews.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/8/17.
 */
public class ArticleAdapter extends CommonAdapter<Article> {


    public ArticleAdapter(Context context,List<Article> articleList,int layoutId){
        super(context,articleList,layoutId);

    }
    @Override
    public void convert(int position, ViewHolder viewHolder, ListView listView, Article article) {
        viewHolder.setText(R.id.title,article.getTitle());
        viewHolder.setText(R.id.likenum,String.valueOf(article.getLikenum()));
        viewHolder.setText(R.id.commentnum,String.valueOf(article.getCommentnum()));
        viewHolder.setText(R.id.source,article.getSource());
        viewHolder.setImage(R.id.photo,article.getPhoto());
    }
}
