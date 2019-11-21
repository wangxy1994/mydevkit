package com.wangxy.devkit.business.market;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class DocModelDaoJdbcTemplateImp implements DocModelDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DocModelDaoJdbcTemplateImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Boolean add(DocModel docModel) {
        String sql = "INSERT INTO tbdocmodel(model_no,model_name) VALUES(?,?)";
        return jdbcTemplate.update(sql, docModel.getModelNo(), docModel.getModelName()) > 0;
    }

    @Override
    public Boolean update(DocModel docModel) {
        String sql = "UPDATE tbdocmodel SET model_name = ? WHERE model_no = ?";
        return jdbcTemplate.update(sql, docModel.getModelName(), docModel.getModelNo()) > 0;
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM tbdocmodel WHERE model_no = ?";
        return jdbcTemplate.update(sql, id) > 0;

    }

    @Override
    public DocModel locate(Long id) {
        String sql = "SELECT * FROM tbdocmodel WHERE model_no=?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);

        if (rs.next()) {
            return generateEntity(rs);
        }
        return null;
    }

    @Override
    public List<DocModel> matchName(String name) {
        String sql = "SELECT * FROM tbdocmodel WHERE model_name = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, name);
        List<DocModel> docModels = new ArrayList<>();
        while (rs.next()) {
            docModels.add(generateEntity(rs));
        }
        return docModels;
    }

    private DocModel generateEntity(SqlRowSet rs) {
        DocModel weChatPay = new DocModel();
        weChatPay.setModelNo(rs.getString("model_no"));
        weChatPay.setModelName(rs.getString("model_name"));
        return weChatPay;
    }
}
