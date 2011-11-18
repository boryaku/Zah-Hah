hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            pooled = true
            dbCreate = "create-drop"
            url = "jdbc:mysql://localhost/sirirock"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "sirirock"
            password = "sirirock"
        }
    }
    test {
        dataSource {
            pooled = true
            dbCreate = "create-drop"
            url = "jdbc:mysql://localhost/sirirock_test"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "sirirock"
            password = "sirirock"
        }
    }
    production {
        dataSource {
            pooled = true
            dbCreate = "update"
            url = "jdbc:mysql://localhost/sirirock"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "sirirock"
            password = "sirirock"
        }
    }
}
