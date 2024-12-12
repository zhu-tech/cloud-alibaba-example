package com.neyogoo.example.admin.sys.fallback;

import com.neyogoo.example.admin.sys.api.OrgQueryApi;
import com.neyogoo.example.admin.sys.vo.response.OrgTreeRespVO;
import com.neyogoo.example.common.core.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 机构查询
 */
@Slf4j
@Component
public class OrgQueryApiFallbackFactory implements FallbackFactory<OrgQueryApi> {

    @Override
    public OrgQueryApi create(Throwable cause) {

        return new OrgQueryApi() {

            @Override
            public R<String> getNameById(Long id) {
                log.error("get org name by id error, id = {}, cause = {}", id, cause.getMessage());
                return R.fail("根据id查询机构名称失败");
            }

            @Override
            public R<Map<Long, String>> mapNamesByIds(Collection<Long> ids) {
                log.error("get org names by ids error, ids = {}, cause = {}", ids, cause.getMessage());
                return R.fail("根据id列表查询机构名称失败");
            }

            @Override
            public R<List<OrgTreeRespVO>> findTree() {
                log.error("find org tree error, cause = {}", cause.getMessage());
                return R.fail("根据机构树失败");
            }
        };
    }
}
