import { Component, OnInit, inject } from '@angular/core';
import { MunicipalStatistics } from '../../models/municipal.model';
import { MunicipalServiceApi } from '../../services/municipal.service';

@Component({
  selector: 'app-statistics',
  imports: [],
  templateUrl: './statistics.html',
  styleUrl: './statistics.css'
})
export class StatisticsComponent implements OnInit {
  private readonly api = inject(MunicipalServiceApi);

  stats: MunicipalStatistics | null = null;
  loading = true;
  errorMessage: string | null = null;

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats(): void {
    this.loading = true;
    this.errorMessage = null;
    this.api.getStatistics().subscribe({
      next: (data) => {
        this.stats = data;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err.message;
        this.loading = false;
      }
    });
  }

  get serviceEntries(): { key: string; value: number }[] {
    if (!this.stats?.requestsByService) return [];
    return Object.entries(this.stats.requestsByService).map(([key, value]) => ({ key, value }));
  }

  get statusEntries(): { key: string; value: number }[] {
    if (!this.stats?.requestsByStatus) return [];
    return Object.entries(this.stats.requestsByStatus).map(([key, value]) => ({ key, value }));
  }

  percentOfTotal(count: number): number {
    if (!this.stats || this.stats.totalRequests === 0) return 0;
    return Math.round((count / this.stats.totalRequests) * 100);
  }
}
