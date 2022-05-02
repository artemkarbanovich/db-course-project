import { Component, OnInit } from '@angular/core';
import { Statistic } from 'src/app/models/statistic';
import { StatisticService } from 'src/app/services/statistic.service';

@Component({
  selector: 'app-statistic',
  templateUrl: './statistic.component.html',
  styleUrls: ['./statistic.component.scss']
})
export class StatisticComponent implements OnInit {
  public stat: Statistic;

  constructor(private statisticService: StatisticService) { }

  public ngOnInit(): void {
    this.getStatistic();
  }

  public getStatistic(): void {
    this.statisticService.getStatistics().subscribe((statistic: Statistic) => {
      this.stat = statistic;
    });
  }
}
