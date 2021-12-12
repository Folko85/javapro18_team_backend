{
  "annotations": {
    "list": [
      {
        "builtIn": 1,
        "datasource": "-- Grafana --",
        "enable": true,
        "hide": true,
        "iconColor": "rgba(0, 211, 255, 1)",
        "name": "Annotations & Alerts",
        "target": {
          "limit": 100,
          "matchAny": false,
          "tags": [],
          "type": "dashboard"
        },
        "type": "dashboard"
      }
    ]
  },
  "description": "",
  "editable": true,
  "fiscalYearStartMonth": 0,
  "gnetId": null,
  "graphTooltip": 0,
  "id": 19,
  "iteration": 1637170075994,
  "links": [],
  "liveNow": false,
  "panels": [
    {
      "cacheTimeout": null,
      "datasource": "Prometheus",
      "description": "",
      "fieldConfig": {
        "defaults": {
          "color": {
            "mode": "thresholds"
          },
          "mappings": [
            {
              "options": {
                "0": {
                  "text": "DOWN"
                },
                "1": {
                  "text": "UP"
                }
              },
              "type": "value"
            },
            {
              "options": {
                "match": "null",
                "result": {
                  "text": "DOWN"
                }
              },
              "type": "special"
            }
          ],
          "thresholds": {
            "mode": "absolute",
            "steps": [
              {
                "color": "green",
                "value": null
              },
              {
                "color": "red",
                "value": 80
              }
            ]
          },
          "unit": "none"
        },
        "overrides": []
      },
      "gridPos": {
        "h": 6,
        "w": 2,
        "x": 0,
        "y": 0
      },
      "id": 25,
      "interval": null,
      "libraryPanel": {
        "description": "",
        "meta": {
          "connectedDashboards": 1,
          "created": "2021-11-16T11:42:06Z",
          "createdBy": {
            "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
            "id": 1,
            "name": "QMIS"
          },
          "folderName": "General",
          "folderUid": "",
          "updated": "2021-11-16T11:53:30Z",
          "updatedBy": {
            "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
            "id": 1,
            "name": "QMIS"
          }
        },
        "name": "Status prod",
        "type": "stat",
        "uid": "QzkGzyc7z",
        "version": 3
      },
      "links": [],
      "maxDataPoints": 100,
      "options": {
        "colorMode": "none",
        "graphMode": "none",
        "justifyMode": "auto",
        "orientation": "horizontal",
        "reduceOptions": {
          "calcs": [
            "mean"
          ],
          "fields": "",
          "values": false
        },
        "text": {},
        "textMode": "auto"
      },
      "pluginVersion": "8.2.1",
      "targets": [
        {
          "expr": "up{application=\"$application\",instance=\"$instance\"}",
          "format": "time_series",
          "intervalFactor": 1,
          "refId": "A"
        }
      ],
      "title": "Status",
      "type": "stat"
    },
    {
      "datasource": null,
      "gridPos": {
        "h": 6,
        "w": 4,
        "x": 2,
        "y": 0
      },
      "id": 5,
      "options": {
        "alertName": "",
        "dashboardAlerts": false,
        "dashboardTitle": "",
        "folderId": null,
        "maxItems": 10,
        "showOptions": "current",
        "sortOrder": 1,
        "stateFilter": {
          "alerting": false,
          "execution_error": false,
          "no_data": false,
          "ok": false,
          "paused": false,
          "pending": false
        },
        "tags": []
      },
      "pluginVersion": "8.2.1",
      "targets": [
        {
          "format": "time_series",
          "group": [],
          "metricColumn": "none",
          "rawQuery": false,
          "rawSql": "SELECT\n  DATEEXECUTED AS \"time\",\n  ORDEREXECUTED\nFROM DATABASECHANGELOG\nWHERE\n  $__timeFilter(DATEEXECUTED)\nORDER BY DATEEXECUTED",
          "refId": "A",
          "select": [
            [
              {
                "params": [
                  "ORDEREXECUTED"
                ],
                "type": "column"
              }
            ]
          ],
          "table": "DATABASECHANGELOG",
          "timeColumn": "DATEEXECUTED",
          "timeColumnType": "timestamp",
          "where": [
            {
              "name": "$__timeFilter",
              "params": [],
              "type": "macro"
            }
          ]
        }
      ],
      "title": "Alerts",
      "type": "alertlist"
    },
    {
      "cacheTimeout": null,
      "datasource": "Prometheus",
      "description": "",
      "fieldConfig": {
        "defaults": {
          "color": {
            "mode": "thresholds"
          },
          "decimals": 1,
          "mappings": [
            {
              "options": {
                "match": "null",
                "result": {
                  "text": "N/A"
                }
              },
              "type": "special"
            }
          ],
          "thresholds": {
            "mode": "absolute",
            "steps": [
              {
                "color": "green",
                "value": null
              },
              {
                "color": "red",
                "value": 80
              }
            ]
          },
          "unit": "s"
        },
        "overrides": []
      },
      "gridPos": {
        "h": 6,
        "w": 6,
        "x": 6,
        "y": 0
      },
      "id": 31,
      "interval": null,
      "libraryPanel": {
        "description": "",
        "meta": {
          "connectedDashboards": 1,
          "created": "2021-11-16T11:42:10Z",
          "createdBy": {
            "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
            "id": 1,
            "name": "QMIS"
          },
          "folderName": "General",
          "folderUid": "",
          "updated": "2021-11-16T11:42:10Z",
          "updatedBy": {
            "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
            "id": 1,
            "name": "QMIS"
          }
        },
        "name": "Up Time",
        "type": "stat",
        "uid": "nD7Gks57k",
        "version": 1
      },
      "links": [],
      "maxDataPoints": 100,
      "options": {
        "colorMode": "value",
        "graphMode": "none",
        "justifyMode": "auto",
        "orientation": "horizontal",
        "reduceOptions": {
          "calcs": [
            "lastNotNull"
          ],
          "fields": "",
          "values": false
        },
        "text": {},
        "textMode": "auto"
      },
      "pluginVersion": "8.2.1",
      "targets": [
        {
          "expr": "process_uptime_seconds{application=\"$application\", instance=\"$instance\"}",
          "format": "time_series",
          "intervalFactor": 1,
          "refId": "A"
        }
      ],
      "title": "Up Time",
      "type": "stat"
    },
    {
      "cacheTimeout": null,
      "datasource": "Prometheus",
      "description": "",
      "fieldConfig": {
        "defaults": {
          "color": {
            "mode": "thresholds"
          },
          "mappings": [
            {
              "options": {
                "match": "null",
                "result": {
                  "text": "N/A"
                }
              },
              "type": "special"
            }
          ],
          "thresholds": {
            "mode": "absolute",
            "steps": [
              {
                "color": "green",
                "value": null
              },
              {
                "color": "red",
                "value": 80
              }
            ]
          },
          "unit": "dateTimeAsIso"
        },
        "overrides": []
      },
      "gridPos": {
        "h": 6,
        "w": 6,
        "x": 12,
        "y": 0
      },
      "id": 51,
      "interval": null,
      "libraryPanel": {
        "description": "",
        "meta": {
          "connectedDashboards": 1,
          "created": "2021-11-16T12:06:32Z",
          "createdBy": {
            "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
            "id": 1,
            "name": "QMIS"
          },
          "folderName": "General",
          "folderUid": "",
          "updated": "2021-11-16T12:18:51Z",
          "updatedBy": {
            "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
            "id": 1,
            "name": "QMIS"
          }
        },
        "name": "Start time",
        "type": "stat",
        "uid": "4TU0msc7z",
        "version": 2
      },
      "links": [],
      "maxDataPoints": 100,
      "options": {
        "colorMode": "value",
        "graphMode": "none",
        "justifyMode": "auto",
        "orientation": "auto",
        "reduceOptions": {
          "calcs": [
            "lastNotNull"
          ],
          "fields": "",
          "values": false
        },
        "text": {
          "titleSize": 1,
          "valueSize": 70
        },
        "textMode": "auto"
      },
      "pluginVersion": "8.2.1",
      "targets": [
        {
          "exemplar": true,
          "expr": "process_start_time_seconds{application=\"$application\", instance=\"$instance\"}*1000",
          "format": "time_series",
          "instant": false,
          "interval": "",
          "intervalFactor": 1,
          "legendFormat": "",
          "metric": "",
          "refId": "A",
          "step": 14400
        }
      ],
      "title": "Start time",
      "type": "stat"
    },
    {
      "datasource": "Prometheus",
      "description": "",
      "fieldConfig": {
        "defaults": {
          "color": {
            "mode": "thresholds"
          },
          "mappings": [],
          "thresholds": {
            "mode": "absolute",
            "steps": [
              {
                "color": "green",
                "value": null
              },
              {
                "color": "red",
                "value": 80
              }
            ]
          }
        },
        "overrides": []
      },
      "gridPos": {
        "h": 6,
        "w": 6,
        "x": 18,
        "y": 0
      },
      "id": 53,
      "libraryPanel": {
        "description": "",
        "meta": {
          "connectedDashboards": 1,
          "created": "2021-11-16T12:27:41Z",
          "createdBy": {
            "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
            "id": 1,
            "name": "QMIS"
          },
          "folderName": "General",
          "folderUid": "",
          "updated": "2021-11-16T12:28:12Z",
          "updatedBy": {
            "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
            "id": 1,
            "name": "QMIS"
          }
        },
        "name": "CPU Load",
        "type": "gauge",
        "uid": "YxoyWy5nk",
        "version": 2
      },
      "links": [],
      "options": {
        "orientation": "auto",
        "reduceOptions": {
          "calcs": [
            "lastNotNull"
          ],
          "fields": "",
          "values": false
        },
        "showThresholdLabels": false,
        "showThresholdMarkers": true,
        "text": {}
      },
      "pluginVersion": "8.2.1",
      "targets": [
        {
          "expr": "system_load_average_1m{application=\"$application\", instance=\"$instance\"}",
          "format": "time_series",
          "intervalFactor": 2,
          "legendFormat": "system-1m",
          "metric": "",
          "refId": "A",
          "step": 2400
        },
        {
          "expr": "system_cpu_count{application=\"$application\", instance=\"$instance\"}",
          "format": "time_series",
          "intervalFactor": 2,
          "legendFormat": "cpus",
          "refId": "B"
        }
      ],
      "timeFrom": null,
      "timeShift": null,
      "title": "Load",
      "type": "gauge"
    },
    {
      "collapsed": true,
      "datasource": null,
      "gridPos": {
        "h": 1,
        "w": 24,
        "x": 0,
        "y": 6
      },
      "id": 7,
      "panels": [
        {
          "datasource": "Loki",
          "gridPos": {
            "h": 15,
            "w": 23,
            "x": 0,
            "y": 7
          },
          "id": 15,
          "options": {
            "dedupStrategy": "none",
            "enableLogDetails": true,
            "prettifyLogMessage": false,
            "showCommonLabels": false,
            "showLabels": false,
            "showTime": false,
            "sortOrder": "Descending",
            "wrapLogMessage": false
          },
          "targets": [
            {
              "expr": "{job=\"varlogs\"}",
              "refId": "A"
            }
          ],
          "title": "Logs",
          "type": "logs"
        },
        {
          "alert": {
            "alertRuleTags": {},
            "conditions": [
              {
                "evaluator": {
                  "params": [
                    20
                  ],
                  "type": "gt"
                },
                "operator": {
                  "type": "and"
                },
                "query": {
                  "params": [
                    "A",
                    "1m",
                    "now"
                  ]
                },
                "reducer": {
                  "params": [],
                  "type": "avg"
                },
                "type": "query"
              }
            ],
            "executionErrorState": "keep_state",
            "for": "0",
            "frequency": "1m",
            "handler": 1,
            "name": "Error alert",
            "noDataState": "ok",
            "notifications": []
          },
          "datasource": "Loki",
          "description": "",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "palette-classic"
              },
              "custom": {
                "axisLabel": "",
                "axisPlacement": "auto",
                "barAlignment": 0,
                "drawStyle": "line",
                "fillOpacity": 15,
                "gradientMode": "none",
                "hideFrom": {
                  "legend": false,
                  "tooltip": false,
                  "viz": false
                },
                "lineInterpolation": "linear",
                "lineStyle": {
                  "fill": "solid"
                },
                "lineWidth": 2,
                "pointSize": 5,
                "scaleDistribution": {
                  "type": "linear"
                },
                "showPoints": "never",
                "spanNulls": true,
                "stacking": {
                  "group": "A",
                  "mode": "normal"
                },
                "thresholdsStyle": {
                  "mode": "off"
                }
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  },
                  {
                    "color": "red",
                    "value": 80
                  }
                ]
              },
              "unit": "short"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 7,
            "w": 11,
            "x": 0,
            "y": 22
          },
          "id": 19,
          "libraryPanel": {
            "description": "",
            "meta": {
              "connectedDashboards": 1,
              "created": "2021-11-16T11:37:55Z",
              "createdBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              },
              "folderName": "General",
              "folderUid": "",
              "updated": "2021-11-16T11:37:55Z",
              "updatedBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              }
            },
            "name": "Error graph",
            "type": "timeseries",
            "uid": "dQKrRyc7k",
            "version": 1
          },
          "options": {
            "legend": {
              "calcs": [
                "max",
                "sum"
              ],
              "displayMode": "list",
              "placement": "bottom"
            },
            "tooltip": {
              "mode": "single"
            }
          },
          "pluginVersion": "8.2.1",
          "targets": [
            {
              "expr": "count_over_time({job=\"varlogs\"}|=\"ERROR\"[5m])",
              "instant": false,
              "legendFormat": "",
              "maxLines": 0,
              "range": true,
              "refId": "A",
              "resolution": 1
            }
          ],
          "thresholds": [
            {
              "colorMode": "critical",
              "op": "gt",
              "value": 20,
              "visible": true
            }
          ],
          "timeFrom": null,
          "timeShift": null,
          "title": "Error",
          "type": "timeseries"
        },
        {
          "alert": {
            "alertRuleTags": {},
            "conditions": [
              {
                "evaluator": {
                  "params": [
                    0
                  ],
                  "type": "gt"
                },
                "operator": {
                  "type": "and"
                },
                "query": {
                  "params": [
                    "A",
                    "1m",
                    "now"
                  ]
                },
                "reducer": {
                  "params": [],
                  "type": "count"
                },
                "type": "query"
              }
            ],
            "executionErrorState": "keep_state",
            "for": "0",
            "frequency": "10m",
            "handler": 1,
            "message": "Liquibase failed !",
            "name": "Liquibase failed alert",
            "noDataState": "keep_state",
            "notifications": []
          },
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": "Loki",
          "description": "",
          "fieldConfig": {
            "defaults": {
              "unit": "none"
            },
            "overrides": []
          },
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 7,
            "w": 13,
            "x": 11,
            "y": 22
          },
          "hiddenSeries": false,
          "id": 21,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": true,
            "total": false,
            "values": false
          },
          "libraryPanel": {
            "description": "",
            "meta": {
              "connectedDashboards": 1,
              "created": "2021-11-16T11:38:01Z",
              "createdBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              },
              "folderName": "General",
              "folderUid": "",
              "updated": "2021-11-16T11:38:01Z",
              "updatedBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              }
            },
            "name": "Liquibase failed",
            "type": "graph",
            "uid": "4BJrRy5nk",
            "version": 1
          },
          "lines": true,
          "linewidth": 1,
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "8.2.1",
          "pointradius": 2,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "expr": "count_over_time({job=\"varlogs\"}|=\"Liquibase failed to start\"[5m])",
              "legendFormat": "",
              "refId": "A"
            }
          ],
          "thresholds": [
            {
              "colorMode": "critical",
              "fill": true,
              "line": true,
              "op": "gt",
              "value": 0,
              "visible": true
            }
          ],
          "timeFrom": null,
          "timeRegions": [],
          "timeShift": null,
          "title": "Liquibase failed",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "buckets": null,
            "mode": "time",
            "name": null,
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "$$hashKey": "object:721",
              "format": "none",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            },
            {
              "$$hashKey": "object:722",
              "format": "short",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            }
          ],
          "yaxis": {
            "align": false,
            "alignLevel": null
          }
        }
      ],
      "title": "Loki (Alerts & Errors from log)",
      "type": "row"
    },
    {
      "collapsed": true,
      "datasource": null,
      "gridPos": {
        "h": 1,
        "w": 24,
        "x": 0,
        "y": 7
      },
      "id": 13,
      "panels": [
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": "Prometheus",
          "description": "",
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 7,
            "w": 11,
            "x": 0,
            "y": 8
          },
          "hiddenSeries": false,
          "id": 23,
          "legend": {
            "avg": false,
            "current": false,
            "hideEmpty": false,
            "hideZero": false,
            "max": false,
            "min": false,
            "show": true,
            "total": false,
            "values": false
          },
          "libraryPanel": {
            "description": "",
            "meta": {
              "connectedDashboards": 1,
              "created": "2021-11-16T11:42:21Z",
              "createdBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              },
              "folderName": "General",
              "folderUid": "",
              "updated": "2021-11-17T08:49:46Z",
              "updatedBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              }
            },
            "name": "Mean response time",
            "type": "graph",
            "uid": "1UTGzyc7k",
            "version": 2
          },
          "lines": true,
          "linewidth": 2,
          "links": [],
          "nullPointMode": "null as zero",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "8.2.1",
          "pointradius": 5,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "expr": "rate(http_server_requests_seconds_sum{application=\"$application\", instance=\"$instance\"}[1m])/rate(http_server_requests_seconds_count{application=\"$application\", instance=\"$instance\"}[1m])",
              "format": "time_series",
              "instant": false,
              "intervalFactor": 1,
              "legendFormat": "{{method}}-{{status}}-{{uri}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeFrom": null,
          "timeRegions": [],
          "timeShift": null,
          "title": "Mean response time",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "buckets": null,
            "mode": "time",
            "name": null,
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "$$hashKey": "object:76",
              "format": "s",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            },
            {
              "$$hashKey": "object:77",
              "format": "short",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            }
          ],
          "yaxis": {
            "align": false,
            "alignLevel": null
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": "Prometheus",
          "description": "",
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 7,
            "w": 13,
            "x": 11,
            "y": 8
          },
          "hiddenSeries": false,
          "id": 29,
          "legend": {
            "avg": false,
            "current": true,
            "max": true,
            "min": false,
            "show": true,
            "total": false,
            "values": true
          },
          "libraryPanel": {
            "description": "",
            "meta": {
              "connectedDashboards": 1,
              "created": "2021-11-16T11:42:15Z",
              "createdBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              },
              "folderName": "General",
              "folderUid": "",
              "updated": "2021-11-16T11:42:15Z",
              "updatedBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              }
            },
            "name": "Requests per second",
            "type": "graph",
            "uid": "UrvGzsc7k",
            "version": 1
          },
          "lines": true,
          "linewidth": 2,
          "links": [],
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "8.2.1",
          "pointradius": 5,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "expr": "rate(http_server_requests_seconds_count{application=\"$application\", instance=\"$instance\"}[1m])",
              "format": "time_series",
              "intervalFactor": 1,
              "legendFormat": "{{method}}-{{status}}-{{uri}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeFrom": null,
          "timeRegions": [],
          "timeShift": null,
          "title": "Requests per second",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "buckets": null,
            "mode": "time",
            "name": null,
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            },
            {
              "format": "short",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            }
          ],
          "yaxis": {
            "align": false,
            "alignLevel": null
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": "Prometheus",
          "description": "",
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 7,
            "w": 11,
            "x": 0,
            "y": 15
          },
          "hiddenSeries": false,
          "id": 27,
          "legend": {
            "avg": false,
            "current": true,
            "max": false,
            "min": false,
            "show": true,
            "total": false,
            "values": true
          },
          "libraryPanel": {
            "description": "",
            "meta": {
              "connectedDashboards": 1,
              "created": "2021-11-16T11:42:27Z",
              "createdBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              },
              "folderName": "General",
              "folderUid": "",
              "updated": "2021-11-16T11:42:27Z",
              "updatedBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              }
            },
            "name": "Response time of 50%, 75%, 90%, 95% of requests",
            "type": "graph",
            "uid": "RmPGzy57z",
            "version": 1
          },
          "lines": true,
          "linewidth": 2,
          "links": [],
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "8.2.1",
          "pointradius": 5,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "expr": "histogram_quantile(0.95, sum(rate(http_server_requests_seconds_bucket{application=\"$application\", instance=\"$instance\"}[1m])) by (le))",
              "format": "time_series",
              "instant": false,
              "intervalFactor": 1,
              "legendFormat": "95%",
              "refId": "A"
            },
            {
              "expr": "histogram_quantile(0.9, sum(rate(http_server_requests_seconds_bucket{application=\"$application\", instance=\"$instance\"}[1m])) by (le))",
              "format": "time_series",
              "intervalFactor": 1,
              "legendFormat": "90%",
              "refId": "B"
            },
            {
              "expr": "histogram_quantile(0.75, sum(rate(http_server_requests_seconds_bucket{application=\"$application\", instance=\"$instance\"}[1m])) by (le))",
              "format": "time_series",
              "intervalFactor": 1,
              "legendFormat": "75%",
              "refId": "C"
            },
            {
              "expr": "histogram_quantile(0.5, sum(rate(http_server_requests_seconds_bucket{application=\"$application\", instance=\"$instance\"}[1m])) by (le))",
              "format": "time_series",
              "intervalFactor": 1,
              "legendFormat": "50%",
              "refId": "D"
            }
          ],
          "thresholds": [],
          "timeFrom": null,
          "timeRegions": [],
          "timeShift": null,
          "title": "Response time of 50%, 75%, 90%, 95% of requests",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "buckets": null,
            "mode": "time",
            "name": null,
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "s",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            },
            {
              "format": "short",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            }
          ],
          "yaxis": {
            "align": false,
            "alignLevel": null
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": "Prometheus",
          "description": "",
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 7,
            "w": 13,
            "x": 11,
            "y": 15
          },
          "hiddenSeries": false,
          "id": 33,
          "legend": {
            "avg": false,
            "current": false,
            "max": false,
            "min": false,
            "show": true,
            "total": false,
            "values": false
          },
          "libraryPanel": {
            "description": "",
            "meta": {
              "connectedDashboards": 1,
              "created": "2021-11-16T11:42:32Z",
              "createdBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              },
              "folderName": "General",
              "folderUid": "",
              "updated": "2021-11-16T11:42:32Z",
              "updatedBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              }
            },
            "name": "Top 10 APIs",
            "type": "graph",
            "uid": "ZFlGky5nk",
            "version": 1
          },
          "lines": true,
          "linewidth": 1,
          "links": [],
          "nullPointMode": "null as zero",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "8.2.1",
          "pointradius": 5,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "expr": "topk(10, sum by(uri, method) (rate(http_server_requests_seconds_count{application=\"$application\"}[1m])))",
              "format": "time_series",
              "intervalFactor": 1,
              "legendFormat": "",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeFrom": null,
          "timeRegions": [],
          "timeShift": null,
          "title": "Top 10 APIs",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "buckets": null,
            "mode": "time",
            "name": null,
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            },
            {
              "format": "short",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            }
          ],
          "yaxis": {
            "align": false,
            "alignLevel": null
          }
        }
      ],
      "title": "Spring Micrometr",
      "type": "row"
    },
    {
      "collapsed": true,
      "datasource": null,
      "gridPos": {
        "h": 1,
        "w": 24,
        "x": 0,
        "y": 8
      },
      "id": 11,
      "panels": [
        {
          "cacheTimeout": null,
          "datasource": "Prometheus",
          "description": "",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "thresholds"
              },
              "decimals": 2,
              "mappings": [
                {
                  "options": {
                    "match": "null",
                    "result": {
                      "text": "N/A"
                    }
                  },
                  "type": "special"
                }
              ],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "rgba(50, 172, 45, 0.97)",
                    "value": null
                  },
                  {
                    "color": "rgba(237, 129, 40, 0.89)",
                    "value": 70
                  },
                  {
                    "color": "rgba(245, 54, 54, 0.9)",
                    "value": 90
                  }
                ]
              },
              "unit": "percent"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 3,
            "x": 0,
            "y": 10
          },
          "id": 35,
          "interval": null,
          "libraryPanel": {
            "description": "",
            "meta": {
              "connectedDashboards": 1,
              "created": "2021-11-16T12:06:07Z",
              "createdBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              },
              "folderName": "General",
              "folderUid": "",
              "updated": "2021-11-16T12:06:07Z",
              "updatedBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              }
            },
            "name": "Heap used",
            "type": "stat",
            "uid": "UbR0my5nk",
            "version": 1
          },
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "value",
            "graphMode": "none",
            "justifyMode": "auto",
            "orientation": "horizontal",
            "reduceOptions": {
              "calcs": [
                "lastNotNull"
              ],
              "fields": "",
              "values": false
            },
            "text": {},
            "textMode": "auto"
          },
          "pluginVersion": "8.2.1",
          "targets": [
            {
              "expr": "sum(jvm_memory_used_bytes{application=\"$application\", instance=\"$instance\", area=\"heap\"})*100/sum(jvm_memory_max_bytes{application=\"$application\",instance=\"$instance\", area=\"heap\"})",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "",
              "refId": "A",
              "step": 14400
            }
          ],
          "title": "Heap used",
          "type": "stat"
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": "Prometheus",
          "description": "",
          "editable": true,
          "error": false,
          "fill": 1,
          "fillGradient": 0,
          "grid": {
            "leftLogBase": 1,
            "leftMax": null,
            "leftMin": null,
            "rightLogBase": 1,
            "rightMax": null,
            "rightMin": null
          },
          "gridPos": {
            "h": 6,
            "w": 10,
            "x": 3,
            "y": 10
          },
          "hiddenSeries": false,
          "id": 37,
          "legend": {
            "avg": false,
            "current": true,
            "max": true,
            "min": false,
            "show": true,
            "total": false,
            "values": true
          },
          "libraryPanel": {
            "description": "",
            "meta": {
              "connectedDashboards": 1,
              "created": "2021-11-16T12:05:50Z",
              "createdBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              },
              "folderName": "General",
              "folderUid": "",
              "updated": "2021-11-16T12:05:50Z",
              "updatedBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              }
            },
            "name": "JVM Heap",
            "type": "graph",
            "uid": "sDooiycnk",
            "version": 1
          },
          "lines": true,
          "linewidth": 1,
          "links": [],
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "8.2.1",
          "pointradius": 5,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "expr": "sum(jvm_memory_used_bytes{application=\"$application\", instance=\"$instance\", area=\"heap\"})",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "used",
              "metric": "",
              "refId": "A",
              "step": 2400
            },
            {
              "expr": "sum(jvm_memory_committed_bytes{application=\"$application\", instance=\"$instance\", area=\"heap\"})",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "committed",
              "refId": "B",
              "step": 2400
            },
            {
              "expr": "sum(jvm_memory_max_bytes{application=\"$application\", instance=\"$instance\", area=\"heap\"})",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "max",
              "refId": "C",
              "step": 2400
            }
          ],
          "thresholds": [],
          "timeFrom": null,
          "timeRegions": [],
          "timeShift": null,
          "title": "JVM Heap",
          "tooltip": {
            "msResolution": false,
            "shared": true,
            "sort": 0,
            "value_type": "cumulative"
          },
          "type": "graph",
          "x-axis": true,
          "xaxis": {
            "buckets": null,
            "mode": "time",
            "name": null,
            "show": true,
            "values": []
          },
          "y-axis": true,
          "y_formats": [
            "mbytes",
            "short"
          ],
          "yaxes": [
            {
              "format": "bytes",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": 0,
              "show": true
            },
            {
              "format": "short",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            }
          ],
          "yaxis": {
            "align": false,
            "alignLevel": null
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": "Prometheus",
          "description": "",
          "editable": true,
          "error": false,
          "fill": 1,
          "fillGradient": 0,
          "grid": {
            "leftLogBase": 1,
            "leftMax": null,
            "leftMin": null,
            "rightLogBase": 1,
            "rightMax": null,
            "rightMin": null
          },
          "gridPos": {
            "h": 6,
            "w": 11,
            "x": 13,
            "y": 10
          },
          "hiddenSeries": false,
          "id": 39,
          "legend": {
            "alignAsTable": false,
            "avg": false,
            "current": true,
            "max": true,
            "min": false,
            "show": true,
            "total": false,
            "values": true
          },
          "libraryPanel": {
            "description": "",
            "meta": {
              "connectedDashboards": 1,
              "created": "2021-11-16T12:05:58Z",
              "createdBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              },
              "folderName": "General",
              "folderUid": "",
              "updated": "2021-11-16T12:05:58Z",
              "updatedBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              }
            },
            "name": "JVM Total",
            "type": "graph",
            "uid": "GR8omscnk",
            "version": 1
          },
          "lines": true,
          "linewidth": 1,
          "links": [],
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "8.2.1",
          "pointradius": 5,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "expr": "sum(jvm_memory_used_bytes{application=\"$application\", instance=\"$instance\"})",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "used",
              "metric": "",
              "refId": "A",
              "step": 2400
            },
            {
              "expr": "sum(jvm_memory_committed_bytes{application=\"$application\", instance=\"$instance\"})",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "committed",
              "refId": "B",
              "step": 2400
            },
            {
              "expr": "sum(jvm_memory_max_bytes{application=\"$application\", instance=\"$instance\"})",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "max",
              "refId": "C",
              "step": 2400
            }
          ],
          "thresholds": [],
          "timeFrom": null,
          "timeRegions": [],
          "timeShift": null,
          "title": "JVM Total",
          "tooltip": {
            "msResolution": false,
            "shared": true,
            "sort": 0,
            "value_type": "cumulative"
          },
          "type": "graph",
          "x-axis": true,
          "xaxis": {
            "buckets": null,
            "mode": "time",
            "name": null,
            "show": true,
            "values": []
          },
          "y-axis": true,
          "y_formats": [
            "mbytes",
            "short"
          ],
          "yaxes": [
            {
              "format": "bytes",
              "label": "",
              "logBase": 1,
              "max": null,
              "min": 0,
              "show": true
            },
            {
              "format": "short",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            }
          ],
          "yaxis": {
            "align": false,
            "alignLevel": null
          }
        },
        {
          "cacheTimeout": null,
          "datasource": "Prometheus",
          "description": "",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "thresholds"
              },
              "decimals": 2,
              "mappings": [
                {
                  "options": {
                    "match": "null",
                    "result": {
                      "text": "N/A"
                    }
                  },
                  "type": "special"
                },
                {
                  "options": {
                    "from": -1e+32,
                    "result": {
                      "text": "N/A"
                    },
                    "to": 0
                  },
                  "type": "range"
                }
              ],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "rgba(50, 172, 45, 0.97)",
                    "value": null
                  },
                  {
                    "color": "rgba(237, 129, 40, 0.89)",
                    "value": 70
                  },
                  {
                    "color": "rgba(245, 54, 54, 0.9)",
                    "value": 90
                  }
                ]
              },
              "unit": "percent"
            },
            "overrides": []
          },
          "gridPos": {
            "h": 3,
            "w": 3,
            "x": 0,
            "y": 13
          },
          "id": 47,
          "interval": null,
          "libraryPanel": {
            "description": "",
            "meta": {
              "connectedDashboards": 1,
              "created": "2021-11-16T12:06:11Z",
              "createdBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              },
              "folderName": "General",
              "folderUid": "",
              "updated": "2021-11-16T12:06:11Z",
              "updatedBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              }
            },
            "name": "Non-Heap used",
            "type": "stat",
            "uid": "ptGAis5nz",
            "version": 1
          },
          "links": [],
          "maxDataPoints": 100,
          "options": {
            "colorMode": "value",
            "graphMode": "none",
            "justifyMode": "auto",
            "orientation": "auto",
            "reduceOptions": {
              "calcs": [
                "lastNotNull"
              ],
              "fields": "",
              "values": false
            },
            "text": {},
            "textMode": "auto"
          },
          "pluginVersion": "8.2.1",
          "targets": [
            {
              "expr": "sum(jvm_memory_used_bytes{application=\"$application\", instance=\"$instance\", area=\"nonheap\"})*100/sum(jvm_memory_max_bytes{application=\"$application\",instance=\"$instance\", area=\"nonheap\"})",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "",
              "refId": "A",
              "step": 14400
            }
          ],
          "title": "Non-Heap used",
          "type": "stat"
        },
        {
          "datasource": "Prometheus",
          "description": "",
          "fieldConfig": {
            "defaults": {
              "color": {
                "mode": "thresholds"
              },
              "mappings": [],
              "thresholds": {
                "mode": "absolute",
                "steps": [
                  {
                    "color": "green",
                    "value": null
                  },
                  {
                    "color": "red",
                    "value": 80
                  }
                ]
              }
            },
            "overrides": []
          },
          "gridPos": {
            "h": 5,
            "w": 4,
            "x": 0,
            "y": 16
          },
          "id": 43,
          "libraryPanel": {
            "description": "",
            "meta": {
              "connectedDashboards": 1,
              "created": "2021-11-16T12:06:16Z",
              "createdBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              },
              "folderName": "General",
              "folderUid": "",
              "updated": "2021-11-16T12:06:16Z",
              "updatedBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              }
            },
            "name": "Load",
            "type": "gauge",
            "uid": "FoDAmy5nk",
            "version": 1
          },
          "links": [],
          "options": {
            "orientation": "auto",
            "reduceOptions": {
              "calcs": [
                "lastNotNull"
              ],
              "fields": "",
              "values": false
            },
            "showThresholdLabels": false,
            "showThresholdMarkers": true,
            "text": {}
          },
          "pluginVersion": "8.2.1",
          "targets": [
            {
              "expr": "system_load_average_1m{application=\"$application\", instance=\"$instance\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "system-1m",
              "metric": "",
              "refId": "A",
              "step": 2400
            },
            {
              "expr": "system_cpu_count{application=\"$application\", instance=\"$instance\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "cpus",
              "refId": "B"
            }
          ],
          "timeFrom": null,
          "timeShift": null,
          "title": "Load",
          "transparent": true,
          "type": "gauge"
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": "Prometheus",
          "description": "",
          "editable": true,
          "error": false,
          "fill": 1,
          "fillGradient": 0,
          "grid": {
            "leftLogBase": 1,
            "leftMax": null,
            "leftMin": null,
            "rightLogBase": 1,
            "rightMax": null,
            "rightMin": null
          },
          "gridPos": {
            "h": 5,
            "w": 20,
            "x": 4,
            "y": 16
          },
          "hiddenSeries": false,
          "id": 41,
          "legend": {
            "avg": false,
            "current": true,
            "max": true,
            "min": false,
            "show": true,
            "total": false,
            "values": true
          },
          "libraryPanel": {
            "description": "",
            "meta": {
              "connectedDashboards": 1,
              "created": "2021-11-16T12:07:03Z",
              "createdBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              },
              "folderName": "General",
              "folderUid": "",
              "updated": "2021-11-16T12:07:03Z",
              "updatedBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              }
            },
            "name": "Load CPU",
            "type": "graph",
            "uid": "LBP1mscnk",
            "version": 1
          },
          "lines": true,
          "linewidth": 1,
          "links": [],
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "8.2.1",
          "pointradius": 5,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "expr": "system_load_average_1m{application=\"$application\", instance=\"$instance\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "system-1m",
              "metric": "",
              "refId": "A",
              "step": 2400
            },
            {
              "expr": "system_cpu_count{application=\"$application\", instance=\"$instance\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "cpus",
              "refId": "B"
            }
          ],
          "thresholds": [],
          "timeFrom": null,
          "timeRegions": [],
          "timeShift": null,
          "title": "Load",
          "tooltip": {
            "msResolution": false,
            "shared": true,
            "sort": 0,
            "value_type": "cumulative"
          },
          "type": "graph",
          "x-axis": true,
          "xaxis": {
            "buckets": null,
            "mode": "time",
            "name": null,
            "show": true,
            "values": []
          },
          "y-axis": true,
          "y_formats": [
            "short",
            "short"
          ],
          "yaxes": [
            {
              "decimals": 1,
              "format": "short",
              "label": "",
              "logBase": 1,
              "max": null,
              "min": 0,
              "show": true
            },
            {
              "format": "short",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            }
          ],
          "yaxis": {
            "align": false,
            "alignLevel": null
          }
        },
        {
          "aliasColors": {},
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": "Prometheus",
          "description": "",
          "editable": true,
          "error": false,
          "fill": 1,
          "fillGradient": 0,
          "grid": {
            "leftLogBase": 1,
            "leftMax": null,
            "leftMin": null,
            "rightLogBase": 1,
            "rightMax": null,
            "rightMin": null
          },
          "gridPos": {
            "h": 6,
            "w": 12,
            "x": 0,
            "y": 21
          },
          "hiddenSeries": false,
          "id": 45,
          "legend": {
            "avg": false,
            "current": true,
            "max": true,
            "min": false,
            "show": true,
            "total": false,
            "values": true
          },
          "libraryPanel": {
            "description": "",
            "meta": {
              "connectedDashboards": 1,
              "created": "2021-11-16T12:06:51Z",
              "createdBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              },
              "folderName": "General",
              "folderUid": "",
              "updated": "2021-11-16T12:06:51Z",
              "updatedBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              }
            },
            "name": "Threads",
            "type": "graph",
            "uid": "xfF1my57k",
            "version": 1
          },
          "lines": true,
          "linewidth": 1,
          "links": [],
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "8.2.1",
          "pointradius": 5,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "expr": "jvm_threads_live_threads{application=\"$application\", instance=\"$instance\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "live",
              "metric": "",
              "refId": "A",
              "step": 2400
            },
            {
              "expr": "jvm_threads_daemon_threads{application=\"$application\", instance=\"$instance\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "daemon",
              "metric": "",
              "refId": "B",
              "step": 2400
            },
            {
              "expr": "jvm_threads_peak_threads{application=\"$application\", instance=\"$instance\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "peak",
              "refId": "C",
              "step": 2400
            },
            {
              "expr": "process_threads{application=\"$application\", instance=\"$instance\"}",
              "format": "time_series",
              "interval": "",
              "intervalFactor": 2,
              "legendFormat": "process",
              "refId": "D",
              "step": 2400
            }
          ],
          "thresholds": [],
          "timeFrom": null,
          "timeRegions": [],
          "timeShift": null,
          "title": "Threads",
          "tooltip": {
            "msResolution": false,
            "shared": true,
            "sort": 0,
            "value_type": "cumulative"
          },
          "type": "graph",
          "x-axis": true,
          "xaxis": {
            "buckets": null,
            "mode": "time",
            "name": null,
            "show": true,
            "values": []
          },
          "y-axis": true,
          "y_formats": [
            "short",
            "short"
          ],
          "yaxes": [
            {
              "decimals": 0,
              "format": "short",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": 0,
              "show": true
            },
            {
              "format": "short",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            }
          ],
          "yaxis": {
            "align": false,
            "alignLevel": null
          }
        },
        {
          "aliasColors": {
            "blocked": "#bf1b00",
            "new": "#fce2de",
            "runnable": "#7eb26d",
            "terminated": "#511749",
            "timed-waiting": "#c15c17",
            "waiting": "#eab839"
          },
          "bars": false,
          "dashLength": 10,
          "dashes": false,
          "datasource": "Prometheus",
          "description": "",
          "fill": 1,
          "fillGradient": 0,
          "gridPos": {
            "h": 6,
            "w": 12,
            "x": 12,
            "y": 21
          },
          "hiddenSeries": false,
          "id": 49,
          "legend": {
            "alignAsTable": false,
            "avg": false,
            "current": true,
            "max": true,
            "min": false,
            "rightSide": false,
            "show": true,
            "total": false,
            "values": true
          },
          "libraryPanel": {
            "description": "",
            "meta": {
              "connectedDashboards": 1,
              "created": "2021-11-16T12:07:46Z",
              "createdBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              },
              "folderName": "General",
              "folderUid": "",
              "updated": "2021-11-16T12:07:46Z",
              "updatedBy": {
                "avatarUrl": "/avatar/675ef481a8fab22de417c36fe01b7046",
                "id": 1,
                "name": "QMIS"
              }
            },
            "name": "Thread States",
            "type": "graph",
            "uid": "BOkaiycnk",
            "version": 1
          },
          "lines": true,
          "linewidth": 1,
          "links": [],
          "nullPointMode": "null",
          "options": {
            "alertThreshold": true
          },
          "percentage": false,
          "pluginVersion": "8.2.1",
          "pointradius": 5,
          "points": false,
          "renderer": "flot",
          "seriesOverrides": [],
          "spaceLength": 10,
          "stack": false,
          "steppedLine": false,
          "targets": [
            {
              "expr": "jvm_threads_states_threads{application=\"$application\", instance=\"$instance\"}",
              "format": "time_series",
              "intervalFactor": 2,
              "legendFormat": "{{state}}",
              "refId": "A"
            }
          ],
          "thresholds": [],
          "timeFrom": null,
          "timeRegions": [],
          "timeShift": null,
          "title": "Thread States",
          "tooltip": {
            "shared": true,
            "sort": 0,
            "value_type": "individual"
          },
          "type": "graph",
          "xaxis": {
            "buckets": null,
            "mode": "time",
            "name": null,
            "show": true,
            "values": []
          },
          "yaxes": [
            {
              "format": "short",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            },
            {
              "format": "short",
              "label": null,
              "logBase": 1,
              "max": null,
              "min": null,
              "show": true
            }
          ],
          "yaxis": {
            "align": false,
            "alignLevel": null
          }
        }
      ],
      "title": "JVM Micrometr",
      "type": "row"
    },
    {
      "collapsed": true,
      "datasource": null,
      "gridPos": {
        "h": 1,
        "w": 24,
        "x": 0,
        "y": 9
      },
      "id": 9,
      "panels": [],
      "title": "DB health",
      "type": "row"
    },
    {
      "collapsed": false,
      "datasource": null,
      "gridPos": {
        "h": 1,
        "w": 24,
        "x": 0,
        "y": 10
      },
      "id": 17,
      "panels": [],
      "title": "Dashboards",
      "type": "row"
    },
    {
      "datasource": null,
      "gridPos": {
        "h": 11,
        "w": 12,
        "x": 0,
        "y": 11
      },
      "id": 3,
      "links": [],
      "options": {
        "folderId": 0,
        "maxItems": 30,
        "query": "",
        "showHeadings": true,
        "showRecentlyViewed": true,
        "showSearch": false,
        "showStarred": true,
        "tags": []
      },
      "pluginVersion": "8.2.1",
      "tags": [],
      "title": "Dashboards",
      "type": "dashlist"
    }
  ],
  "refresh": false,
  "schemaVersion": 31,
  "style": "dark",
  "tags": [],
  "templating": {
    "list": [
      {
        "allValue": null,
        "current": {
          "selected": false,
          "text": "147.78.64.57:8086",
          "value": "147.78.64.57:8086"
        },
        "datasource": "Prometheus",
        "definition": "label_values(up{application=\"$application\"}, instance)",
        "description": null,
        "error": null,
        "hide": 0,
        "includeAll": false,
        "label": "instance",
        "multi": false,
        "name": "instance",
        "options": [],
        "query": {
          "query": "label_values(up{application=\"$application\"}, instance)",
          "refId": "StandardVariableQuery"
        },
        "refresh": 2,
        "regex": "",
        "skipUrlSync": false,
        "sort": 0,
        "type": "query"
      },
      {
        "allValue": null,
        "current": {
          "isNone": true,
          "selected": false,
          "text": "None",
          "value": ""
        },
        "datasource": "Prometheus",
        "definition": "label_values(application)",
        "description": null,
        "error": null,
        "hide": 0,
        "includeAll": false,
        "label": "Application",
        "multi": false,
        "name": "application",
        "options": [],
        "query": {
          "query": "label_values(application)",
          "refId": "StandardVariableQuery"
        },
        "refresh": 2,
        "regex": "",
        "skipUrlSync": false,
        "sort": 0,
        "type": "query"
      }
    ]
  },
  "time": {
    "from": "now-6h",
    "to": "now"
  },
  "timepicker": {
    "hidden": true,
    "refresh_intervals": [
      "5s",
      "10s",
      "30s",
      "1m",
      "5m",
      "15m",
      "30m",
      "1h",
      "2h",
      "1d"
    ],
    "time_options": [
      "5m",
      "15m",
      "1h",
      "6h",
      "12h",
      "24h",
      "2d",
      "7d",
      "30d"
    ],
    "type": "timepicker"
  },
  "timezone": "browser",
  "title": "Zeronenetwork.design",
  "uid": "OCx7gy57z",
  "version": 22
}