/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.neyogoo.example.common.uid.dao;

import com.baidu.fsg.uid.worker.entity.WorkerNodeEntity;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerNodeDao {

    @Insert("INSERT INTO m_worker_node(host_name, port, type, launch_date, update_time, create_time) "
            + "VALUES (#{hostName}, #{port}, #{type}, #{launchDate}, #{updateTime}, #{createTime})")
    void addWorkerNode(WorkerNodeEntity workerNodeEntity);
}
